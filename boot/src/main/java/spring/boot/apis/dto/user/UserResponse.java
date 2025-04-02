package spring.boot.apis.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.dto.device.DeviceResponse;
import spring.boot.apis.dto.role.RoleResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  Long id;
  String username;
  @JsonIgnore String password;

  Set<RoleResponse> roles;
  DeviceResponse device;
}
