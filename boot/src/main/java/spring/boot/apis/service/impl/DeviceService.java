package spring.boot.apis.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.dto.device.DeviceCreate;
import spring.boot.apis.dto.device.DeviceResponse;
import spring.boot.apis.dto.device.DeviceUpdate;
import spring.boot.apis.mapper.DeviceMapper;
import spring.boot.apis.model.Device;
import spring.boot.apis.model.User;
import spring.boot.apis.repository.DeviceRepository;
import spring.boot.apis.repository.UserRepository;
import spring.boot.apis.service.IDeviceService;
import spring.boot.constant.HttpMessage;
import spring.boot.exception.ServiceException;
import spring.boot.response.MultiResource;
import spring.boot.response.Paging;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeviceService implements IDeviceService {
  DeviceRepository deviceRepository;
  UserRepository userRepository;
  DeviceMapper deviceMapper;

  @Override
  public Device existedById(Long id) {
    Optional<Device> old = deviceRepository.findById(id);
    if (!old.isPresent()) {
      throw new ServiceException(HttpMessage.FIND_ALL_BY_ID_NO_CONTENT);
    }
    return old.get();
  }

  @Override
  public User owningExistedById(Long id) {
    var owning = userRepository.findById(id);
    if (!owning.isPresent())
      throw new ServiceException(HttpMessage.OWNING_SIDE_NOT_EXISTED);
    return owning.get();
  }

  @Override
  public DeviceResponse save(DeviceCreate creation) {
    Device model = deviceMapper.asModel(creation);
    var owning = owningExistedById(creation.getUserId());
    model.setUser(owning);
    Device saved = deviceRepository.save(model);
    return deviceMapper.asResponse(saved);
  }

  @Override
  public DeviceResponse findById(Long id) {
    Device queried = existedById(id);
    return deviceMapper.asResponse(queried);
  }

  @Override
  public MultiResource<DeviceResponse> findAll(int page, int size) {
    var pageable = deviceRepository.findAll(PageRequest.of(page, size));
    List<DeviceResponse> values = deviceMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public MultiResource<DeviceResponse> findAllArchived(int page, int size) {
    var pageable = deviceRepository.findAllByDeletedIsTrue(PageRequest.of(page, size));
    List<DeviceResponse> values = deviceMapper.asResponses(pageable.getContent());
    if (CollectionUtils.isEmpty(values))
      throw new ServiceException(HttpMessage.FIND_ALL_NO_CONTENT);
    Paging paging = new Paging(page, pageable.getTotalPages(), pageable.getTotalElements());
    return new MultiResource<>(values, paging);
  }

  @Override
  public DeviceResponse updateById(Long id, DeviceUpdate update) {
    Device old = existedById(id);
    Device model = deviceRepository.save(old);
    return deviceMapper.asResponse(model);
  }

  @Override
  public DeviceResponse deleteById(Long id) {
    Device old = existedById(id);
    DeviceResponse response = deviceMapper.asResponse(old);
    deviceRepository.delete(old);
    return response;
  }
}
