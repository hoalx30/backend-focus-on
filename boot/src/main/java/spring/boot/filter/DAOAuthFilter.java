package spring.boot.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import spring.boot.apis.dto.auth.UserPrincipal;
import spring.boot.apis.dto.user.UserResponse;
import spring.boot.apis.provider.ITokenBasedProvider;
import spring.boot.apis.repository.BadCredentialRepository;
import spring.boot.apis.service.IUserService;
import spring.boot.constant.HttpMessage;
import spring.boot.exception.ServiceException;
import spring.boot.response.Response;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DAOAuthFilter extends OncePerRequestFilter {
    @NonFinal
    @Value("${spring.security.accessTokenSecret}")
    String accessTokenSecret;

    ObjectMapper objectMapper;
    IUserService userService;
    ITokenBasedProvider<SignedJWT, UserResponse> jwtProvider;
    BadCredentialRepository badCredentialRepository;

    // @formatter:off
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException { // @formatter:on
        List<String> publicEndpoints = List.of("/api/v1/auth/sign-in", "/api/v1/auth/sign-up");
        if (publicEndpoints.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            if (StringUtils.isEmpty(request.getHeader(HttpHeaders.AUTHORIZATION)))
                throw new ServiceException(HttpMessage.MISSING_AUTHORIZATION);
            String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
            SignedJWT claims = jwtProvider.verify(accessToken, accessTokenSecret);
            String username = claims.getJWTClaimsSet().getSubject();
            if (StringUtils.isEmpty(username))
                throw new ServiceException(HttpMessage.ILL_LEGAL_TOKEN);
            boolean isBlocked = badCredentialRepository.existsByAccessTokenId(claims.getJWTClaimsSet().getJWTID());
            if (isBlocked) {
                throw new ServiceException(HttpMessage.TOKEN_BLOCKED);
            }
            if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // @formatter:off
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // @formatter:on
                authentication.setDetails(buildUserDetail(claims));
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
            filterChain.doFilter(request, response);
        } catch (ServiceException exception) {
            HttpMessage causeBy = exception.getCauseBy();
            terminate(response, causeBy.getHttpStatus().value(), causeBy.getCode(), causeBy.getMessage());
        } catch (Exception exception) {
            HttpMessage causeBy = HttpMessage.UNCATEGORIZED;
            System.out.println("Exception: " + exception.getMessage());
            terminate(response, causeBy.getHttpStatus().value(), causeBy.getCode(), causeBy.getMessage());
        }
    }

    private void terminate(HttpServletResponse response, int httpStatus, int code, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus);
        var resp = Response.builder().code(code).message(message).build();
        response.getWriter().write(objectMapper.writeValueAsString(resp));
        response.flushBuffer();
    }

    private HashMap<String, Object> buildUserDetail(SignedJWT signedJWT) throws Exception {
        HashMap<String, Object> details = new HashMap<>();
        String username = signedJWT.getJWTClaimsSet().getSubject();
        long userId = signedJWT.getJWTClaimsSet().getLongClaim("userId");
        String sessionId = signedJWT.getJWTClaimsSet().getJWTID();
        Date sessionExpiredAt = signedJWT.getJWTClaimsSet().getExpirationTime();
        String scope = signedJWT.getJWTClaimsSet().getStringClaim("scope");
        String referId = signedJWT.getJWTClaimsSet().getStringClaim("referId");
        details.put("user", new UserPrincipal(username, userId, sessionId, sessionExpiredAt, scope, referId));
        return details;
    }
}
