package spring.boot.apis.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.dto.profile.ProfileCreate;
import spring.boot.apis.dto.profile.ProfileResponse;
import spring.boot.apis.dto.profile.ProfileUpdate;
import spring.boot.apis.mapper.ProfileMapper;
import spring.boot.apis.model.Profile;
import spring.boot.apis.model.User;
import spring.boot.apis.repository.ProfileRepository;
import spring.boot.apis.repository.UserRepository;
import spring.boot.apis.service.IProfileService;
import spring.boot.constant.HttpMessage;
import spring.boot.exception.ServiceException;
import spring.boot.response.MultiResource;
import spring.boot.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService implements IProfileService {
  ProfileRepository statusRepository;
  UserRepository userRepository;
  ProfileMapper statusMapper;

  @Override
  public Profile existedById(Long id) {
    Optional<Profile> old = statusRepository.findById(id);
    if (!old.isPresent()) {
      throw new ServiceException(HttpMessage.FIND_ALL_BY_ID_NO_CONTENT);
    }
    return old.get();
  }

  @Override
  public User owningExistedById(Long id) {
    Optional<User> owning = userRepository.findById(id);
    if (!owning.isPresent())
      throw new ServiceException(HttpMessage.OWNING_SIDE_NOT_EXISTED);
    return owning.get();
  }

  @Override
  public void owningAvailableById(Long id) {
    Optional<Profile> existed = statusRepository.findByUserId(id);
    if (existed.isPresent()) {
      throw new ServiceException(HttpMessage.OWNING_SIDE_NOT_AVAILABLE);
    }
  }

  // Testing for Spring Security Authorization Only
  @PreAuthorize("hasAnyAuthority('CREATE','ROLE_ADMIN')")
  @Override
  public ProfileResponse save(ProfileCreate create) {
    Profile model = statusMapper.asModel(create);
    var owning = owningExistedById(create.getUserId());
    owningAvailableById(create.getUserId());
    model.setUser(owning);
    Profile saved = statusRepository.save(model);
    return statusMapper.asResponse(saved);
  }

  @Override
  public ProfileResponse findById(Long id) {
    Profile queried = existedById(id);
    return statusMapper.asResponse(queried);
  }

  @Override
  public MultiResource<ProfileResponse> findAll(int page, int size) {
    var pageable = statusRepository.findAll(PageRequest.of(page, size));
    List<ProfileResponse> values = statusMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public MultiResource<ProfileResponse> findAllArchived(int page, int size) {
    var pageable = statusRepository.findAllByDeletedIsTrue(PageRequest.of(page, size));
    List<ProfileResponse> values = statusMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public ProfileResponse updateById(Long id, ProfileUpdate update) {
    Profile old = existedById(id);
    statusMapper.mergeModel(old, update);
    Profile model = statusRepository.save(old);
    return statusMapper.asResponse(model);
  }

  @Override
  public ProfileResponse deleteById(Long id) {
    Profile old = existedById(id);
    ProfileResponse response = statusMapper.asResponse(old);
    statusRepository.delete(old);
    return response;
  }
}
