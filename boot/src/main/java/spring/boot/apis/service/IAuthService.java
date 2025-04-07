package spring.boot.apis.service;

import spring.boot.apis.dto.auth.CredentialRequest;
import spring.boot.apis.dto.auth.CredentialResponse;
import spring.boot.apis.dto.auth.RegisterRequest;
import spring.boot.apis.dto.auth.RegisterResponse;
import spring.boot.apis.model.BadCredential;

public interface IAuthService {
  CredentialResponse signUp(RegisterRequest request);

  CredentialResponse signIn(CredentialRequest request);

  RegisterResponse me(String username);

  long signOut(BadCredential model);

  CredentialResponse refresh(BadCredential badCredential, String referId, String refreshToken);

  CredentialResponse signUpGoogle(String code);
}
