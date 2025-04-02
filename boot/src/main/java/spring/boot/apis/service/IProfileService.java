package spring.boot.apis.service;

import spring.boot.apis.dto.profile.ProfileCreate;
import spring.boot.apis.dto.profile.ProfileResponse;
import spring.boot.apis.dto.profile.ProfileUpdate;
import spring.boot.apis.model.Profile;
import spring.boot.apis.model.User;
import spring.boot.response.MultiResource;

public interface IProfileService {
  Profile existedById(Long id);

  User owningExistedById(Long id);

  void owningAvailableById(Long id);

  ProfileResponse save(ProfileCreate create);

  ProfileResponse findById(Long id);

  MultiResource<ProfileResponse> findAll(int page, int size);

  MultiResource<ProfileResponse> findAllArchived(int page, int size);

  ProfileResponse updateById(Long id, ProfileUpdate update);

  ProfileResponse deleteById(Long id);
}
