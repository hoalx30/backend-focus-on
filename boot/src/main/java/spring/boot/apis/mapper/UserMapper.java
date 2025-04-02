package spring.boot.apis.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.boot.apis.dto.user.UserCreate;
import spring.boot.apis.dto.user.UserResponse;
import spring.boot.apis.dto.user.UserUpdate;
import spring.boot.apis.model.User;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
  User asModel(UserCreate create);

  UserResponse asResponse(User model);

  List<UserResponse> asResponses(List<User> models);

  void mergeModel(@MappingTarget User model, UserUpdate update);
}
