package spring.boot.apis.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.boot.apis.dto.device.DeviceCreate;
import spring.boot.apis.dto.device.DeviceResponse;
import spring.boot.apis.dto.device.DeviceUpdate;
import spring.boot.apis.service.IDeviceService;
import spring.boot.constant.HttpMessage;
import spring.boot.response.MultiResource;
import spring.boot.response.Response;

@RestController
@RequestMapping(path = "/api/v1/devices")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeviceController {
  IDeviceService deviceService;

  @PostMapping
  public ResponseEntity<Response<Long>> save(@RequestBody @Valid DeviceCreate create) {
    HttpMessage created = HttpMessage.SAVE;
    DeviceResponse saved = deviceService.save(create);
    var response = Response.<Long>builder()
        .code(created.getCode())
        .message(created.getMessage())
        .payload(saved.getId())
        .build();
    return ResponseEntity.status(created.getHttpStatus()).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Response<DeviceResponse>> findById(
      @PathVariable(name = "id", required = true) Long id) {
    HttpMessage ok = HttpMessage.FIND_BY_ID;
    DeviceResponse queried = deviceService.findById(id);
    Response<DeviceResponse> response = Response.<DeviceResponse>builder()
        .code(ok.getCode())
        .message(ok.getMessage())
        .payload(queried)
        .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping
  public ResponseEntity<Response<List<DeviceResponse>>> findAll(
      @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "page index must be start from 0.") int page,
      @RequestParam(name = "size", defaultValue = "50") @Min(value = 5, message = "page size must be greater than 5.") @Max(value = 50, message = "page size must be less than 50.") int size) {
    HttpMessage ok = HttpMessage.FIND_ALL;
    MultiResource<DeviceResponse> queried = deviceService.findAll(page, size);
    var response = Response.<List<DeviceResponse>>builder()
        .code(ok.getCode())
        .message(ok.getMessage())
        .payload(queried.getValues())
        .paging(queried.getPaging())
        .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/archived")
  public ResponseEntity<Response<List<DeviceResponse>>> findAllByDeletedTrue(
      @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "page index must be start from 0.") int page,
      @RequestParam(name = "size", defaultValue = "50") @Min(value = 5, message = "page size must be greater than 5.") @Max(value = 50, message = "page size must be less than 50.") int size) {
    HttpMessage ok = HttpMessage.FIND_ALL;
    MultiResource<DeviceResponse> queried = deviceService.findAllArchived(page, size);
    var response = Response.<List<DeviceResponse>>builder()
        .code(ok.getCode())
        .message(ok.getMessage())
        .payload(queried.getValues())
        .paging(queried.getPaging())
        .build();
    return ResponseEntity.ok().body(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Response<Long>> updateById(
      @PathVariable(name = "id", required = true) Long id, @RequestBody DeviceUpdate update) {
    HttpMessage ok = HttpMessage.UPDATE;
    DeviceResponse latest = deviceService.updateById(id, update);
    var response = Response.<Long>builder()
        .code(ok.getCode())
        .message(ok.getMessage())
        .payload(latest.getId())
        .build();
    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Response<Long>> deleteById(
      @PathVariable(name = "id", required = true) Long id) {
    HttpMessage ok = HttpMessage.DELETE;
    DeviceResponse old = deviceService.deleteById(id);
    var response = Response.<Long>builder()
        .code(ok.getCode())
        .message(ok.getMessage())
        .payload(old.getId())
        .build();
    return ResponseEntity.ok().body(response);
  }
}
