package spring.boot.apis.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.dto.privilege.PrivilegeCreate;
import spring.boot.apis.dto.privilege.PrivilegeResponse;
import spring.boot.apis.dto.privilege.PrivilegeUpdate;
import spring.boot.apis.mapper.PrivilegeMapper;
import spring.boot.apis.model.Privilege;
import spring.boot.apis.repository.PrivilegeRepository;
import spring.boot.apis.service.IPrivilegeService;
import spring.boot.constant.HttpMessage;
import spring.boot.exception.ServiceException;
import spring.boot.response.MultiResource;
import spring.boot.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivilegeService implements IPrivilegeService {
  PrivilegeRepository privilegeRepository;
  PrivilegeMapper privilegeMapper;

  @Override
  public void notExistedByName(String name) {
    if (privilegeRepository.countExistedByName(name) > 0)
      throw new ServiceException(HttpMessage.ALREADY_EXISTED);
  }

  @Override
  public Privilege existedById(Long id) {
    Optional<Privilege> old = privilegeRepository.findById(id);
    if (!old.isPresent())
      throw new ServiceException(HttpMessage.FIND_BY_ID_NO_CONTENT);
    return old.get();
  }

  @Override
  public PrivilegeResponse save(PrivilegeCreate create) {
    notExistedByName(create.getName());
    Privilege model = privilegeMapper.asModel(create);
    Privilege saved = privilegeRepository.save(model);
    return privilegeMapper.asResponse(saved);
  }

  @Override
  public PrivilegeResponse findById(Long id) {
    Privilege queried = existedById(id);
    return privilegeMapper.asResponse(queried);
  }

  @Override
  public MultiResource<PrivilegeResponse> findAll(int page, int size) {
    var pageable = privilegeRepository.findAll(PageRequest.of(page, size));
    List<PrivilegeResponse> values = privilegeMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public MultiResource<PrivilegeResponse> findAllArchived(int page, int size) {
    var pageable = privilegeRepository.findAllByDeletedIsTrue(PageRequest.of(page, size));
    List<PrivilegeResponse> values = privilegeMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public PrivilegeResponse updateById(Long id, PrivilegeUpdate update) {
    Privilege old = existedById(id);
    privilegeMapper.mergeModel(old, update);
    Privilege model = privilegeRepository.save(old);
    return privilegeMapper.asResponse(model);
  }

  @Override
  public PrivilegeResponse deleteById(Long id) {
    Privilege old = existedById(id);
    PrivilegeResponse response = privilegeMapper.asResponse(old);
    privilegeRepository.deleteById(id);
    return response;
  }
}
