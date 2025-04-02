package spring.boot.apis.dto.role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.validator.UpperCase;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreate {
  @UpperCase(message = "name must be in uppercase")
  @NotBlank(message = "name can not be blank")
  String name;

  @NotBlank(message = "description can not be blank")
  String description;

  @NotEmpty(message = "privilegeIds can not be empty")
  Set<Long> privilegeIds = new HashSet<>(List.of(1L));
}
