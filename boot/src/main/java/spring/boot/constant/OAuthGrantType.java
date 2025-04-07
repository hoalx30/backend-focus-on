package spring.boot.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OAuthGrantType {
  PASSWORD("password"),
  CLIENT_CREDENTIALS("client_credentials"),
  IMPLICIT("implicit"),
  AUTHORIZATION_CODE("authorization_code"),
  ;

  String value;
}
