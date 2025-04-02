package spring.boot.apis.service;

import spring.boot.apis.dto.privilege.PrivilegeCreate;
import spring.boot.apis.dto.privilege.PrivilegeResponse;
import spring.boot.apis.dto.privilege.PrivilegeUpdate;
import spring.boot.apis.model.Privilege;
import spring.boot.response.MultiResource;

public interface IPrivilegeService {
  void notExistedByName(String name);

  Privilege existedById(Long id);

  PrivilegeResponse save(PrivilegeCreate create);

  PrivilegeResponse findById(Long id);

  MultiResource<PrivilegeResponse> findAll(int page, int size);

  MultiResource<PrivilegeResponse> findAllArchived(int page, int size);

  PrivilegeResponse updateById(Long id, PrivilegeUpdate update);

  PrivilegeResponse deleteById(Long id);
}
