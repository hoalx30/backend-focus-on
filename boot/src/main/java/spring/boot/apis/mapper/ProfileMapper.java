package spring.boot.apis.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.boot.apis.dto.profile.ProfileCreate;
import spring.boot.apis.dto.profile.ProfileResponse;
import spring.boot.apis.dto.profile.ProfileUpdate;
import spring.boot.apis.model.Profile;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileMapper {
  Profile asModel(ProfileCreate create);

  ProfileResponse asResponse(Profile model);

  List<ProfileResponse> asResponses(List<Profile> models);

  void mergeModel(@MappingTarget Profile model, ProfileUpdate update);
}
