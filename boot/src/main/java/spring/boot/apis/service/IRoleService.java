package spring.boot.apis.service;

import java.util.Set;
import spring.boot.apis.dto.role.RoleCreate;
import spring.boot.apis.dto.role.RoleResponse;
import spring.boot.apis.dto.role.RoleUpdate;
import spring.boot.apis.model.Privilege;
import spring.boot.apis.model.Role;
import spring.boot.response.MultiResource;

public interface IRoleService {
  void notExistedByName(String name);

  Role existedById(Long id);

  Set<Privilege> owningExistedByIds(Set<Long> ids);

  RoleResponse save(RoleCreate create);

  RoleResponse findById(Long id);

  MultiResource<RoleResponse> findAll(int page, int size);

  MultiResource<RoleResponse> findAllArchived(int page, int size);

  RoleResponse updateById(Long id, RoleUpdate update);

  RoleResponse deleteById(Long id);
}
