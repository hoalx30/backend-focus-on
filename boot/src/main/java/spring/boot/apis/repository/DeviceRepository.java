package spring.boot.apis.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import spring.boot.apis.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
  @Query(value = "SELECT * FROM Device WHERE deleted = TRUE", nativeQuery = true)
  Page<Device> findAllByDeletedIsTrue(Pageable pageable);

  Optional<Device> findByIpAddress(String ipAddress);
}
