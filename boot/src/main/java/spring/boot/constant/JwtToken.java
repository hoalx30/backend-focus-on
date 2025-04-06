package spring.boot.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum JwtToken {
  TOKEN_EXPIRED("Jwt expired"),
  ILL_LEGAL_TOKEN("Malformed payload"),
  ;

  String value;
}
