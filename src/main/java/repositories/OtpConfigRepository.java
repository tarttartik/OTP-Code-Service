package repositories;

import entities.OtpConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpConfigRepository extends JpaRepository<OtpConfig, Long> {
}