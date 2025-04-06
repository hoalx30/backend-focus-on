package spring.boot.apis.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import spring.boot.apis.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
  @Query(value = "SELECT * FROM Profile WHERE deleted = TRUE", nativeQuery = true)
  Page<Profile> findAllByDeletedIsTrue(Pageable pageable);

  Optional<Profile> findByUserId(Long userId);
}
