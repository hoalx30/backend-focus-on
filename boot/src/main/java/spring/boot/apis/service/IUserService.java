package spring.boot.apis.service;

import java.util.Set;
import org.springframework.security.core.userdetails.UserDetailsService;
import spring.boot.apis.dto.user.UserCreate;
import spring.boot.apis.dto.user.UserResponse;
import spring.boot.apis.dto.user.UserUpdate;
import spring.boot.apis.model.Role;
import spring.boot.apis.model.User;
import spring.boot.response.MultiResource;

public interface IUserService {
  UserDetailsService userDetailsService();

  void notExistedByUsername(String username);

  User existedById(Long id);

  Set<Role> owningExistedByIds(Set<Long> ids);

  UserResponse save(UserCreate create);

  UserResponse findById(Long id);

  UserResponse findByUsername(String username);

  MultiResource<UserResponse> findAll(int page, int size);

  MultiResource<UserResponse> findAllArchived(int page, int size);

  UserResponse updateById(Long id, UserUpdate update);

  UserResponse deleteById(Long id);
}
