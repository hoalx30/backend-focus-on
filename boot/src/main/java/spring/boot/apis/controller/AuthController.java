package spring.boot.apis.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.dto.auth.CredentialRequest;
import spring.boot.apis.dto.auth.CredentialResponse;
import spring.boot.apis.dto.auth.RegisterRequest;
import spring.boot.apis.dto.auth.RegisterResponse;
import spring.boot.apis.dto.auth.UserPrincipal;
import spring.boot.apis.model.BadCredential;
import spring.boot.apis.service.IAuthService;
import spring.boot.constant.HttpMessage;
import spring.boot.response.Response;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    IAuthService authService;

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

    @PostMapping("/sign-out")
    public ResponseEntity<Response<Long>> signOut(Authentication authentication) {
        HttpMessage created = HttpMessage.SIGN_OUT;
        @SuppressWarnings("unchecked")
        var user = ((UserPrincipal) ((HashMap<String, Object>) authentication.getDetails()).get("user"));
        var badCredential = new BadCredential(null, user.getSessionId(), user.getSessionExpiredAt(), user.getUserId());
        long id = authService.signOut(badCredential);
        Response<Long> response = Response.<Long>builder()
                .code(created.getCode())
                .message(created.getMessage())
                .payload(id)
                .build();
        return ResponseEntity.status(created.getHttpStatus()).body(response);
    }

    @PostMapping("/refresh")
    // @formatter:off
    public ResponseEntity<Response<CredentialResponse>> refresh(@RequestHeader(value = "X-REFRESH-TOKEN", required = true) String refreshToken, Authentication authentication) { // @formatter:on
        HttpMessage ok = HttpMessage.REFRESH_TOKEN;
        refreshToken = refreshToken.replace("Bearer ", "");
        @SuppressWarnings("unchecked")
        var user = ((UserPrincipal) ((HashMap<String, Object>) authentication.getDetails()).get("user"));
        var badCredential = new BadCredential(null, user.getSessionId(), user.getSessionExpiredAt(), user.getUserId());
        CredentialResponse credential = authService.refresh(badCredential, user.getReferId(), refreshToken);
        Response<CredentialResponse> response = Response.<CredentialResponse>builder()
                .code(ok.getCode())
                .message(ok.getMessage())
                .payload(credential)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
