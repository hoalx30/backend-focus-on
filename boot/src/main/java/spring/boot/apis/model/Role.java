package spring.boot.apis.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.NumericBooleanConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.introspect.RoleIntrospect;
import spring.boot.apis.validator.UpperCase;

@Entity
@EntityListeners(value = RoleIntrospect.class)
@Table(name = "role")
@SoftDelete
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "users", "privileges" })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @UpperCase(message = "name must be in uppercase")
  @NotBlank(message = "name can not be blank")
  @Column(name = "name", unique = true, nullable = false)
  String name;

  @NotBlank(message = "description can not be blank")
  @Column(name = "description", nullable = false)
  String description;

  @CreationTimestamp
  LocalDateTime created;
  @UpdateTimestamp
  LocalDateTime updated;

  @ManyToMany(mappedBy = "roles")
  Set<User> users;

  @ManyToMany(fetch = FetchType.EAGER)
  @SoftDelete(strategy = SoftDeleteType.ACTIVE, converter = NumericBooleanConverter.class)
  Set<Privilege> privileges;
}
