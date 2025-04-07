package spring.boot.apis.repository.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import feign.QueryMap;
import spring.boot.apis.dto.auth.GoogleTokenExchangeRequest;
import spring.boot.apis.dto.auth.GoogleTokenExchangeResponse;

@FeignClient(name = "google-oauth", url = "https://oauth2.googleapis.com")
public interface GoogleOAuthClient {
  @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  GoogleTokenExchangeResponse exchangeToken(@QueryMap GoogleTokenExchangeRequest request);
}
