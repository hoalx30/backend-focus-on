package spring.boot.apis.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import spring.boot.apis.dto.auth.CredentialRequest;
import spring.boot.apis.dto.auth.CredentialResponse;
import spring.boot.apis.dto.auth.RegisterRequest;
import spring.boot.apis.dto.auth.RegisterResponse;
import spring.boot.apis.model.BadCredential;
import spring.boot.apis.service.IAuthService;
import spring.boot.constant.HttpMessage;
import spring.boot.response.Response;

@Tag(name = "Authentication Controller", description = "Authentication controller on API")
@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
  ClientRegistrationRepository clientRegistrationRepository;

  IAuthService authService;

  @NonFinal
  @Value("${spring.security.gapi.clientId}")
  String gapiClientId;

  @NonFinal
  @Value("${spring.security.gapi.clientSecret}")
  String gapiClientSecret;

  @NonFinal
  @Value("${spring.security.gapi.redirectUrl}")
  String gapiRedirectUrl;

  @Operation(summary = "Sign-up", description = "Sign-up endpoint for new user")
  @ApiResponse(responseCode = "200", description = "Ok: Success", content = {
      @Content(schema = @Schema(implementation = CredentialResponse.class), mediaType = "application/json")
  })
  @ApiResponse(responseCode = "400", description = "Bad request: missing username, password, roleIds, username already exists, etc...", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @PostMapping("/sign-up")
  public ResponseEntity<Response<CredentialResponse>> signUp(@RequestBody @Valid RegisterRequest request) {
    HttpMessage created = HttpMessage.SIGN_UP;
    CredentialResponse credential = authService.signUp(request);
    var response = Response.<CredentialResponse>builder()
        .code(created.getCode())
        .message(created.getMessage())
        .payload(credential)
        .build();
    return ResponseEntity.status(created.getHttpStatus()).body(response);
  }

  @Operation(summary = "Sign-in", description = "Sign-in endpoint for existing user")
  @ApiResponse(responseCode = "200", description = "Ok: Success", content = {
      @Content(schema = @Schema(implementation = CredentialResponse.class), mediaType = "application/json")
  })
  @ApiResponse(responseCode = "400", description = "Bad request: missing username, password, username not exists, etc...", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @PostMapping("/sign-in")
  public ResponseEntity<Response<CredentialResponse>> signIn(@RequestBody @Valid CredentialRequest request) {
    HttpMessage ok = HttpMessage.SIGN_IN;
    CredentialResponse credential = authService.signIn(request);
    var response = Response.<CredentialResponse>builder()
        .code(ok.getCode())
        .message(ok.getMessage())
        .payload(credential)
        .build();
    return ResponseEntity.ok().body(response);
  }

  @Operation(summary = "Retrieve profile", description = "Retrieve profile endpoint for existing user")
  @ApiResponse(responseCode = "200", description = "Ok: Success", content = {
      @Content(schema = @Schema(implementation = RegisterRequest.class), mediaType = "application/json")
  })
  @ApiResponse(responseCode = "401", description = "Unauthorized: missing accessToken, token is ill legal, etc...", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @SecurityRequirement(name = "Authorization")
  @GetMapping("/me")
  public ResponseEntity<Response<RegisterResponse>> me(Authentication authentication) {
    HttpMessage ok = HttpMessage.RETRIEVE_PROFILE;
    RegisterResponse user = authService.me(authentication.getName());
    var response = Response.<RegisterResponse>builder()
        .code(ok.getCode())
        .message(ok.getMessage())
        .payload(user)
        .build();
    return ResponseEntity.ok().body(response);
  }

  @Operation(summary = "Sign out", description = "Sign out endpoint for logon user")
  @ApiResponse(responseCode = "200", description = "Ok: Success", content = {
      @Content(schema = @Schema(implementation = Long.class), mediaType = "application/json")
  })
  @ApiResponse(responseCode = "401", description = "Unauthorized: missing accessToken, token is ill legal, etc...", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @SecurityRequirement(name = "Authorization")
  @PostMapping("/sign-out")
  public ResponseEntity<Response<Long>> signOut(Authentication authentication) {
    HttpMessage created = HttpMessage.SIGN_OUT;
    Jwt claims = (Jwt) authentication.getPrincipal();
    var badCredential = new BadCredential(null, claims.getId(), Date.from(claims.getExpiresAt()),
        claims.getClaim("userId"));
    long id = authService.signOut(badCredential);
    Response<Long> response = Response.<Long>builder()
        .code(created.getCode())
        .message(created.getMessage())
        .payload(id)
        .build();
    return ResponseEntity.status(created.getHttpStatus()).body(response);
  }

  @Operation(summary = "Refresh token", description = "Refresh token endpoint for existing user")
  @ApiResponse(responseCode = "200", description = "Ok: Success", content = {
      @Content(schema = @Schema(implementation = RegisterResponse.class), mediaType = "application/json")
  })
  @ApiResponse(responseCode = "401", description = "Unauthorized: missing accessToken, fresh token, token is ill legal, edited, etc...", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
      @Content(schema = @Schema(), mediaType = "application/json") })
  @SecurityRequirement(name = "Authorization")
  @PostMapping("/refresh")
  // @formatter:off
    public ResponseEntity<Response<CredentialResponse>> refresh(@RequestHeader(value = "X-REFRESH-TOKEN", required = true) String refreshToken, Authentication authentication) { // @formatter:on
    HttpMessage ok = HttpMessage.REFRESH_TOKEN;
    refreshToken = refreshToken.replace("Bearer ", "");
    Jwt claims = (Jwt) authentication.getPrincipal();
    var badCredential = new BadCredential(null, claims.getId(), Date.from(claims.getExpiresAt()),
        claims.getClaim("userId"));
    CredentialResponse credential = authService.refresh(badCredential, claims.getClaimAsString("referId"),
        refreshToken);
    Response<CredentialResponse> response = Response.<CredentialResponse>builder()
        .code(ok.getCode())
        .message(ok.getMessage())
        .payload(credential)
        .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/oauth/authorize")
  public void authorize(HttpServletResponse response) throws IOException {
    // @formatter:off
    String oauthUrl = "https://accounts.google.com/o/oauth2/v2/auth" + "?client_id=" + gapiClientId + "&redirect_uri=" + URLEncoder.encode(gapiRedirectUrl, StandardCharsets.UTF_8) + "&response_type=code" + "&scope=" + URLEncoder.encode("openid profile email", StandardCharsets.UTF_8) + "&access_type=offline" + "&prompt=consent"; // @formatter:on
    response.sendRedirect(oauthUrl);
  }

  @GetMapping("/oauth/callback")
  public ResponseEntity<Response<CredentialResponse>> signUpGoogle(@RequestParam("code") String code) {
    HttpMessage created = HttpMessage.SIGN_UP_WITH_GOOGLE;
    CredentialResponse credential = authService.signUpGoogle(code);
    var response = Response.<CredentialResponse>builder()
        .code(created.getCode())
        .message(created.getMessage())
        .payload(credential)
        .build();
    return ResponseEntity.status(created.getHttpStatus()).body(response);
  }

  @GetMapping("/oauth2/authorize")
  public void authorization(HttpServletResponse response) throws IOException {
    ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
    String authorizationUri = clientRegistration.getProviderDetails().getAuthorizationUri();
    String redirectUri = clientRegistration.getRedirectUri();
    String scope = String.join(" ", clientRegistration.getScopes());
    String clientId = clientRegistration.getClientId();
    // @formatter:off
    String oauthUrl = authorizationUri + "?response_type=code" + "&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope; // @formatter:on
    response.sendRedirect(oauthUrl);
  }

  @GetMapping("/oauth2/callback")
  // @formatter:off
  public ResponseEntity<Response<CredentialResponse>> signUpGoogleOAuth2(@AuthenticationPrincipal OAuth2User oauth2User) { // @formatter:on
    HttpMessage created = HttpMessage.SIGN_UP_WITH_GOOGLE;
    CredentialResponse credential = authService.signUpGoogleOAuth2(oauth2User);
    var response = Response.<CredentialResponse>builder()
        .code(created.getCode())
        .message(created.getMessage())
        .payload(credential)
        .build();
    return ResponseEntity.status(created.getHttpStatus()).body(response);
  }
}
