package spring.boot.apis.introspect;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.boot.apis.model.User;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserIntrospect {
  PasswordEncoder passwordEncoder;

  @PrePersist
  @PreUpdate
  void encryptPassword(User user) {
    Pattern bcryptPattern = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    if (!bcryptPattern.matcher(user.getPassword()).matches()) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
  }
}
