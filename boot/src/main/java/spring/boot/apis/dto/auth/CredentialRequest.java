package spring.boot.apis.dto.auth;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class CredentialRequest {
  @Parameter(name = "username", description = "username", required = true, example = "hoalx1")
  @Schema(name = "username", description = "username", type = "string")
  @NotBlank(message = "username can not be blank")
  String username;

  @Parameter(name = "password", description = "password", required = true, example = "hoalx1")
  @Schema(name = "password", description = "password", type = "string")
  @NotBlank(message = "password can not be blank")
  String password;
}
