package spring.boot.apis.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import spring.boot.apis.dto.role.RoleCreate;
import spring.boot.apis.dto.role.RoleResponse;
import spring.boot.apis.dto.role.RoleUpdate;
import spring.boot.apis.mapper.RoleMapper;
import spring.boot.apis.model.Privilege;
import spring.boot.apis.model.Role;
import spring.boot.apis.repository.PrivilegeRepository;
import spring.boot.apis.repository.RoleRepository;
import spring.boot.apis.service.IRoleService;
import spring.boot.constant.HttpMessage;
import spring.boot.exception.ServiceException;
import spring.boot.response.MultiResource;
import spring.boot.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements IRoleService {
  RoleRepository roleRepository;
  PrivilegeRepository privilegeRepository;
  RoleMapper roleMapper;

  @Override
  public void notExistedByName(String name) {
    if (roleRepository.countExistedByName(name) > 0)
      throw new ServiceException(HttpMessage.ALREADY_EXISTED);
  }

  @Override
  public Role existedById(Long id) {
    Optional<Role> queried = roleRepository.findById(id);
    if (!queried.isPresent()) throw new ServiceException(HttpMessage.FIND_BY_ID_NO_CONTENT);
    return queried.get();
  }

  @Override
  public Set<Privilege> owningExistedByIds(Set<Long> ids) {
    var owning = privilegeRepository.findAllById(ids);
    if (CollectionUtils.isEmpty(owning))
      throw new ServiceException(HttpMessage.OWNING_SIDE_NOT_EXISTED);
    return new HashSet<>(owning);
  }

  @Override
  public RoleResponse save(RoleCreate create) {
    notExistedByName(create.getName());
    Role model = roleMapper.asModel(create);
    var owning = owningExistedByIds(create.getPrivilegeIds());
    model.setPrivileges(owning);
    Role saved = roleRepository.save(model);
    return roleMapper.asResponse(saved);
  }

  @Override
  public RoleResponse findById(Long id) {
    Role queried = existedById(id);
    return roleMapper.asResponse(queried);
  }

  @Override
  public MultiResource<RoleResponse> findAll(int page, int size) {
    var pageable = roleRepository.findAll(PageRequest.of(page, size));
    List<RoleResponse> values = roleMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public MultiResource<RoleResponse> findAllArchived(int page, int size) {
    var pageable = roleRepository.findAllByDeletedIsTrue(PageRequest.of(page, size));
    List<RoleResponse> values = roleMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public RoleResponse updateById(Long id, RoleUpdate update) {
    Role old = existedById(id);
    roleMapper.mergeModel(old, update);
    Role model = roleRepository.save(old);
    return roleMapper.asResponse(model);
  }

  @Override
  public RoleResponse deleteById(Long id) {
    Role old = existedById(id);
    RoleResponse response = roleMapper.asResponse(old);
    roleRepository.delete(old);
    return response;
  }
}
