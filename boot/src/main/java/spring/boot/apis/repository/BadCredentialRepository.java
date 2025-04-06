package spring.boot.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.boot.apis.model.BadCredential;

public interface BadCredentialRepository extends JpaRepository<BadCredential, Long> {
  boolean existsByAccessTokenId(String id);
}
