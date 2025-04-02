package spring.boot.apis.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import spring.boot.apis.dto.user.UserCreate;
import spring.boot.apis.dto.user.UserResponse;
import spring.boot.apis.dto.user.UserUpdate;
import spring.boot.apis.mapper.UserMapper;
import spring.boot.apis.model.Role;
import spring.boot.apis.model.User;
import spring.boot.apis.repository.RoleRepository;
import spring.boot.apis.repository.UserRepository;
import spring.boot.apis.service.IUserService;
import spring.boot.constant.HttpMessage;
import spring.boot.exception.ServiceException;
import spring.boot.response.MultiResource;
import spring.boot.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
  @Override
  public UserDetailsService userDetailsService() {
    return username ->
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(HttpMessage.BAD_CREDENTIAL.getMessage()));
  }

  UserRepository userRepository;
  RoleRepository roleRepository;
  UserMapper userMapper;

  @Override
  public void notExistedByUsername(String username) {
    if (userRepository.countExistedByUsername(username) > 0)
      throw new ServiceException(HttpMessage.ALREADY_EXISTED);
  }

  @Override
  public User existedById(Long id) {
    Optional<User> old = userRepository.findById(id);
    if (!old.isPresent()) throw new ServiceException(HttpMessage.FIND_BY_ID_NO_CONTENT);
    return old.get();
  }

  @Override
  public Set<Role> owningExistedByIds(Set<Long> ids) {
    var owning = roleRepository.findAllById(ids);
    if (CollectionUtils.isEmpty(owning))
      throw new ServiceException(HttpMessage.OWNING_SIDE_NOT_EXISTED);
    return new HashSet<>(owning);
  }

  @Override
  public UserResponse save(UserCreate create) {
    notExistedByUsername(create.getUsername());
    User model = userMapper.asModel(create);
    var owning = owningExistedByIds(create.getRoleIds());
    model.setRoles(owning);
    User saved = userRepository.save(model);
    return userMapper.asResponse(saved);
  }

  @Override
  public UserResponse findById(Long id) {
    User queried = existedById(id);
    return userMapper.asResponse(queried);
  }

  @Override
  public UserResponse findByUsername(String username) {
    Optional<User> queried = userRepository.findByUsername(username);
    if (!queried.isPresent()) throw new ServiceException(HttpMessage.NOT_EXISTED);
    return userMapper.asResponse(queried.get());
  }

  @Override
  public MultiResource<UserResponse> findAll(int page, int size) {
    var pageable = userRepository.findAll(PageRequest.of(page, size));
    List<UserResponse> values = userMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public MultiResource<UserResponse> findAllArchived(int page, int size) {
    var pageable = userRepository.findAllByDeletedIsTrue(PageRequest.of(page, size));
    List<UserResponse> values = userMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public UserResponse updateById(Long id, UserUpdate update) {
    User old = existedById(id);
    userMapper.mergeModel(old, update);
    User model = userRepository.save(old);
    return userMapper.asResponse(model);
  }

  @Override
  public UserResponse deleteById(Long id) {
    User old = existedById(id);
    UserResponse response = userMapper.asResponse(old);
    userRepository.delete(old);
    return response;
  }
}
