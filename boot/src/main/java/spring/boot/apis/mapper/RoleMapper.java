package spring.boot.apis.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import spring.boot.apis.dto.role.RoleCreate;
import spring.boot.apis.dto.role.RoleResponse;
import spring.boot.apis.dto.role.RoleUpdate;
import spring.boot.apis.model.Role;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
  Role asModel(RoleCreate create);

  RoleResponse asResponse(Role model);

  List<RoleResponse> asResponses(List<Role> models);

  void mergeModel(@MappingTarget Role model, RoleUpdate update);
}
