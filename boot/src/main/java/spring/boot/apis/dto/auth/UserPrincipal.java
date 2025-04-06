package spring.boot.apis.dto.auth;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPrincipal {
  String username;
  long userId;
  String sessionId;
  Date sessionExpiredAt;
  String scope;
  String referId;
}
