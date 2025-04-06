package spring.boot.apis.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import spring.boot.apis.dto.auth.CredentialRequest;
import spring.boot.apis.dto.auth.CredentialResponse;
import spring.boot.apis.dto.auth.RegisterRequest;
import spring.boot.apis.dto.auth.RegisterResponse;
import spring.boot.apis.dto.user.UserCreate;
import spring.boot.apis.dto.user.UserResponse;
import spring.boot.apis.mapper.AuthMapper;
import spring.boot.apis.model.BadCredential;
import spring.boot.apis.provider.ITokenBasedProvider;
import spring.boot.apis.repository.BadCredentialRepository;
import spring.boot.apis.service.IAuthService;
import spring.boot.apis.service.IUserService;
import spring.boot.constant.HttpMessage;
import spring.boot.exception.ServiceException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService implements IAuthService {
  @NonFinal
  @Value("${spring.security.accessTokenSecret}")
  String accessTokenSecret;

  @NonFinal
  @Value("${spring.security.accessTokenTtl}")
  long accessTokenTtl;

  @NonFinal
  @Value("${spring.security.refreshTokenSecret}")
  String refreshTokenSecret;

  @NonFinal
  @Value("${spring.security.refreshTokenTtl}")
  long refreshTokenTtl;

  PasswordEncoder passwordEncoder;

  ITokenBasedProvider<SignedJWT, UserResponse> jwtProvider;
  IUserService userService;
  BadCredentialRepository badCredentialRepository;
  AuthMapper authMapper;

  public CredentialResponse newCredentials(UserResponse user) {
    String accessId = UUID.randomUUID().toString();
    String refreshId = UUID.randomUUID().toString();
    String accessToken = jwtProvider.sign(user, accessTokenTtl, accessTokenSecret, accessId, refreshId);
    long accessTokenIssuedAt = System.currentTimeMillis();
    String refreshToken = jwtProvider.sign(user, refreshTokenTtl, refreshTokenSecret, refreshId, accessId);
    long refreshTokenIssuedAt = System.currentTimeMillis();
    return new CredentialResponse(accessToken, accessTokenIssuedAt, refreshToken, refreshTokenIssuedAt);
  }

  @Override
  public CredentialResponse signUp(RegisterRequest request) {
    UserCreate create = authMapper.asUserCreate(request);
    try {
      UserResponse user = userService.save(create);
      return newCredentials(user);
    } catch (ServiceException e) {
      throw new ServiceException(HttpMessage.USER_EXISTED);
    }
  }

  @Override
  public CredentialResponse signIn(CredentialRequest request) {
    try {
      UserResponse user = userService.findByUsername(request.getUsername());
      boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
      if (!isMatch)
        throw new ServiceException(HttpMessage.BAD_CREDENTIAL);
      return newCredentials(user);
    } catch (ServiceException e) {
      throw new ServiceException(HttpMessage.BAD_CREDENTIAL);
    }
  }

  @Override
  public RegisterResponse me(String username) {
    UserResponse user = userService.findByUsername(username);
    return authMapper.asRegisterResponse(user);
  }

  @Override
  public long signOut(BadCredential badCredential) {
    BadCredential saved = badCredentialRepository.save(badCredential);
    return saved.getId();
  }

  @Override
  public CredentialResponse refresh(BadCredential badCredential, String referId, String refreshToken) {
    var claims = jwtProvider.verify(refreshToken, refreshTokenSecret);
    boolean isSuitable;
    try {
      isSuitable = referId.equals(claims.getJWTClaimsSet().getJWTID());
    } catch (Exception e) {
      throw new ServiceException(HttpMessage.ILL_LEGAL_TOKEN);
    }
    if (!isSuitable)
      throw new ServiceException(HttpMessage.TOKEN_NOT_SUITABLE);
    BadCredential saved = badCredentialRepository.save(badCredential);
    UserResponse user = userService.findById(saved.getUserId());
    return newCredentials(user);
  }
}
