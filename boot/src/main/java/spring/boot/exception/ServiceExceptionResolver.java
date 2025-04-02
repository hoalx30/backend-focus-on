package spring.boot.exception;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import spring.boot.constant.HttpMessage;
import spring.boot.response.Response;

@ControllerAdvice
public class ServiceExceptionResolver {
  @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Response<Object>> methodNotAllowed(HttpRequestMethodNotSupportedException exception) {
    HttpMessage methodNotAllowed = HttpMessage.METHOD_NOT_ALLOWED;
    var response = Response.builder()
        .code(methodNotAllowed.getCode())
        .message(methodNotAllowed.getMessage())
        .build();
    return ResponseEntity.status(methodNotAllowed.getHttpStatus()).body(response);
  }

  @ExceptionHandler(value = NoResourceFoundException.class)
  public ResponseEntity<Response<Object>> notFound(NoResourceFoundException exception) {
    HttpMessage notFound = HttpMessage.NOT_FOUND;
    var response = Response.builder().code(notFound.getCode()).message(notFound.getMessage()).build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Response<Object>> illLegalPayload(MethodArgumentNotValidException exception) {
    HttpMessage badRequest = HttpMessage.ILL_LEGAL_PAYLOAD;
    String message = exception.getAllErrors().get(0).getDefaultMessage();
    var response = Response.builder().code(badRequest.getCode()).message(message).build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = HandlerMethodValidationException.class)
  public ResponseEntity<Response<Object>> illLegalPayload(HandlerMethodValidationException exception) {
    HttpMessage badRequest = HttpMessage.ILL_LEGAL_PAYLOAD;
    String message = exception.getAllErrors().get(0).getDefaultMessage();
    var response = Response.builder().code(badRequest.getCode()).message(message).build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = MissingServletRequestParameterException.class)
  public ResponseEntity<Response<Object>> paramNotReadable(MissingServletRequestParameterException exception) {
    HttpMessage badRequest = HttpMessage.ILL_LEGAL_PARAMETER;
    String message = MessageFormat.format("{0} is missing or not readable.", exception.getParameterName());
    var response = Response.builder().code(badRequest.getCode()).message(message).build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Response<Object>> paramNotReadable(MethodArgumentTypeMismatchException exception) {
    HttpMessage badRequest = HttpMessage.ILL_LEGAL_PARAMETER;
    String message = MessageFormat.format("{0} must be in legal format.", exception.getPropertyName());
    var response = Response.builder().code(badRequest.getCode()).message(message).build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = HttpMessageNotReadableException.class)
  public ResponseEntity<Response<Object>> payloadNotReadable(HttpMessageNotReadableException exception) {
    HttpMessage badRequest = HttpMessage.ILL_LEGAL_PAYLOAD;
    var response = Response.builder().code(badRequest.getCode()).message(badRequest.getMessage()).build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = MissingRequestHeaderException.class)
  public ResponseEntity<Response<Object>> headerNotReadable(MissingRequestHeaderException exception) {
    HttpMessage badRequest = HttpMessage.ILL_LEGAL_HEADER;
    String message = MessageFormat.format("{0} is missing or not readable.", exception.getHeaderName());
    var response = Response.builder().code(badRequest.getCode()).message(message).build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(value = AuthorizationDeniedException.class)
  public ResponseEntity<Response<Object>> forbidden(AuthorizationDeniedException exception) {
    HttpMessage forbidden = HttpMessage.FORBIDDEN;
    var response = Response.builder().code(forbidden.getCode()).message(forbidden.getMessage()).build();
    return ResponseEntity.status(forbidden.getHttpStatus()).body(response);
  }

  @ExceptionHandler(value = ServiceException.class)
  public ResponseEntity<Response<Object>> service(ServiceException exception) {
    HttpMessage causeBy = exception.getCauseBy();
    Response<Object> response = Response.builder().code(causeBy.getCode()).message(causeBy.getMessage()).build();
    return ResponseEntity.status(causeBy.getHttpStatus()).body(response);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<Response<Object>> uncategorized(Exception exception) {
    HttpMessage uncategorized = HttpMessage.UNCATEGORIZED;
    System.out.println("Exception: " + exception.getMessage());
    var response = Response.builder()
        .code(uncategorized.getCode())
        .message(uncategorized.getMessage())
        .build();
    return ResponseEntity.status(uncategorized.getHttpStatus()).body(response);
  }
}
