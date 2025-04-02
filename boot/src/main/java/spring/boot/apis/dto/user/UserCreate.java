package spring.boot.apis.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreate {
  @NotBlank(message = "username can not be blank")
  String username;

  @NotBlank(message = "password can not be blank")
  String password;

  @NotEmpty(message = "roleIds can not be empty")
  Set<Long> roleIds = new HashSet<>(List.of(1L));
}
