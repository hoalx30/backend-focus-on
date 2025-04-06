package spring.boot.apis.dto.auth;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CredentialResponse {
  @Parameter(name = "accessToken", description = "accessToken", required = true, example = "accessToken")
  @Schema(name = "accessToken", description = "accessToken", type = "string")
  String accessToken;

  @Parameter(name = "accessTokenIssueAt", description = "accessTokenIssueAt", required = true, example = "0")
  @Schema(name = "accessTokenIssueAt", description = "accessTokenIssueAt", type = "long")
  long accessTokenIssuedAt;

  @Parameter(name = "refreshToken", description = "refreshToken", required = true, example = "refreshToken")
  @Schema(name = "refreshToken", description = "refreshToken", type = "string")
  String refreshToken;

  @Parameter(name = "refreshTokenIssueAt", description = "refreshTokenIssueAt", required = true, example = "0")
  @Schema(name = "refreshTokenIssueAt", description = "refreshTokenIssueAt", type = "long")
  long refreshTokenIssuedAt;
}
