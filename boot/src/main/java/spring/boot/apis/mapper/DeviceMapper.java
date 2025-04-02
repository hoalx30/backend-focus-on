package spring.boot.apis.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.boot.apis.dto.device.DeviceCreate;
import spring.boot.apis.dto.device.DeviceResponse;
import spring.boot.apis.dto.device.DeviceUpdate;
import spring.boot.apis.model.Device;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeviceMapper {
  Device asModel(DeviceCreate create);

  DeviceResponse asResponse(Device model);

  List<DeviceResponse> asResponses(List<Device> models);

  void mergeModel(@MappingTarget Device model, DeviceUpdate update);
}
