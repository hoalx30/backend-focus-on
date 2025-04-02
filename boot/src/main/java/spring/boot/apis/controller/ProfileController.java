package spring.boot.apis.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
import spring.boot.apis.dto.profile.ProfileCreate;
import spring.boot.apis.dto.profile.ProfileResponse;
import spring.boot.apis.dto.profile.ProfileUpdate;
import spring.boot.apis.service.IProfileService;
import spring.boot.constant.HttpMessage;
import spring.boot.response.MultiResource;
import spring.boot.response.Response;

@RestController
@RequestMapping(path = "/api/v1/profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
  IProfileService statusService;

  @PostMapping
  public ResponseEntity<Response<Long>> save(@RequestBody @Valid ProfileCreate create) {
    HttpMessage created = HttpMessage.SAVE;
    ProfileResponse saved = statusService.save(create);
    var response =
        Response.<Long>builder()
            .code(created.getCode())
            .message(created.getMessage())
            .payload(saved.getId())
            .build();
    return ResponseEntity.status(created.getHttpStatus()).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Response<ProfileResponse>> findById(
      @PathVariable(name = "id", required = true) Long id) {
    HttpMessage ok = HttpMessage.FIND_BY_ID;
    ProfileResponse queried = statusService.findById(id);
    var response =
        Response.<ProfileResponse>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .payload(queried)
            .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping
  public ResponseEntity<Response<List<ProfileResponse>>> findAll(
      @RequestParam(name = "page", defaultValue = "0")
          @Min(value = 0, message = "page index must be start from 0.")
          int page,
      @RequestParam(name = "size", defaultValue = "50")
          @Min(value = 5, message = "page size must be greater than 5.")
          @Max(value = 50, message = "page size must be less than 50.")
          int size) {
    HttpMessage ok = HttpMessage.FIND_ALL;
    MultiResource<ProfileResponse> queried = statusService.findAll(page, size);
    var response =
        Response.<List<ProfileResponse>>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .payload(queried.getValues())
            .paging(queried.getPaging())
            .build();
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/archived")
  public ResponseEntity<Response<List<ProfileResponse>>> findAllByDeletedTrue(
      @RequestParam(name = "page", defaultValue = "0")
          @Min(value = 0, message = "page index must be start from 0.")
          int page,
      @RequestParam(name = "size", defaultValue = "50")
          @Min(value = 5, message = "page size must be greater than 5.")
          @Max(value = 50, message = "page size must be less than 50.")
          int size) {
    HttpMessage ok = HttpMessage.FIND_ALL;
    MultiResource<ProfileResponse> queried = statusService.findAllArchived(page, size);
    var response =
        Response.<List<ProfileResponse>>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .payload(queried.getValues())
            .paging(queried.getPaging())
            .build();
    return ResponseEntity.ok().body(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Response<Long>> updateById(
      @PathVariable(name = "id", required = true) Long id, @RequestBody ProfileUpdate update) {
    HttpMessage ok = HttpMessage.UPDATE;
    ProfileResponse latest = statusService.updateById(id, update);
    var response =
        Response.<Long>builder()
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
    ProfileResponse old = statusService.deleteById(id);
    var response =
        Response.<Long>builder()
            .code(ok.getCode())
            .message(ok.getMessage())
            .payload(old.getId())
            .build();
    return ResponseEntity.ok().body(response);
  }
}
