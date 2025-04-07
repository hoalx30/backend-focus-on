package spring.boot.apis.repository.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.boot.apis.dto.auth.GoogleUser;

@FeignClient(name = "google-user", url = "https://www.googleapis.com")
public interface GoogleUserClient {
  @GetMapping("/oauth2/v2/userinfo")
  GoogleUser userInfo(@RequestParam("access_token") String authorization);
}
