package spring.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfigurer {
  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
