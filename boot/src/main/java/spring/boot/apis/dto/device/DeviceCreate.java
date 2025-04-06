package spring.boot.apis.dto.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceCreate {
  @NotBlank(message = "ipAddress can not be blank")
  String ipAddress;

  @NotBlank(message = "userAgent can not be blank")
  String userAgent;

  @NotNull(message = "userId can not be blank")
  Long userId;
}
