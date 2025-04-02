package spring.boot.apis.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.boot.apis.dto.privilege.PrivilegeCreate;
import spring.boot.apis.dto.privilege.PrivilegeResponse;
import spring.boot.apis.dto.privilege.PrivilegeUpdate;
import spring.boot.apis.model.Privilege;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrivilegeMapper {
  Privilege asModel(PrivilegeCreate create);

  PrivilegeResponse asResponse(Privilege model);

  List<PrivilegeResponse> asResponses(List<Privilege> models);

  void mergeModel(@MappingTarget Privilege model, PrivilegeUpdate update);
}
