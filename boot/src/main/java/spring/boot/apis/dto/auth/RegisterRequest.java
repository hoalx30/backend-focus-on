package spring.boot.apis.dto.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank(message = "username can not be blank")
    String username;

    @NotBlank(message = "password can not be blank")
    String password;

    Set<Long> roleIds = new HashSet<>(List.of(1L));
}
