package spring.boot.exception;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.constant.HttpMessage;
import spring.boot.constant.JwtToken;
import spring.boot.response.Response;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceServerEntryPoint implements AuthenticationEntryPoint {
  ObjectMapper objectMapper;

  @Override
  // @formatter:off
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException { // @formatter:on
    String message = authException.getMessage();
    System.out.println("Exception: " + message);
    // @formatter:off
        HttpMessage unauthorized = StringUtils.isEmpty(request.getHeader(HttpHeaders.AUTHORIZATION)) ? HttpMessage.MISSING_AUTHORIZATION : HttpMessage.UNAUTHORIZED; // @formatter:on
    if (message.contains(JwtToken.TOKEN_EXPIRED.getValue())) {
      unauthorized = HttpMessage.TOKEN_EXPIRED;
    } else if (message.contains(JwtToken.ILL_LEGAL_TOKEN.getValue())) {
      unauthorized = HttpMessage.ILL_LEGAL_TOKEN;
    }
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    var resp = Response.builder().code(unauthorized.getCode()).message(unauthorized.getMessage()).build();
    response.getWriter().write(objectMapper.writeValueAsString(resp));
    response.flushBuffer();
  }
}
