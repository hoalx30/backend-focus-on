package spring.boot.apis.dto.auth;

import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.dto.role.RoleResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    Long id;
    String username;
    Set<RoleResponse> roles;
}
