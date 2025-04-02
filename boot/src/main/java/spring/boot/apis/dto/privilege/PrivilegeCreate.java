package spring.boot.apis.dto.privilege;

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
public class PrivilegeCreate {
  @NotBlank(message = "name can not be blank")
  String name;

  @NotBlank(message = "description can not be blank")
  String description;
}
