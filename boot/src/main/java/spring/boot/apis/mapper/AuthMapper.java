package spring.boot.apis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import spring.boot.apis.dto.auth.CredentialRequest;
import spring.boot.apis.dto.auth.RegisterRequest;
import spring.boot.apis.dto.auth.RegisterResponse;
import spring.boot.apis.dto.user.UserCreate;
import spring.boot.apis.dto.user.UserResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {
  UserCreate asUserCreate(RegisterRequest request);

  UserCreate asUserCreate(CredentialRequest request);

  RegisterResponse asRegisterResponse(UserResponse response);
}
