package spring.boot.apis.service;

import spring.boot.apis.dto.device.DeviceCreate;
import spring.boot.apis.dto.device.DeviceResponse;
import spring.boot.apis.dto.device.DeviceUpdate;
import spring.boot.apis.model.Device;
import spring.boot.apis.model.User;
import spring.boot.response.MultiResource;

public interface IDeviceService {
  Device existedById(Long id);

  User owningExistedById(Long id);

  DeviceResponse save(DeviceCreate creation);

  DeviceResponse findById(Long id);

  MultiResource<DeviceResponse> findAll(int page, int size);

  MultiResource<DeviceResponse> findAllArchived(int page, int size);

  DeviceResponse updateById(Long id, DeviceUpdate update);

  DeviceResponse deleteById(Long id);
}
