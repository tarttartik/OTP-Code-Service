package repositories;

import entities.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findByCode(String code);
    Optional<OtpCode> findByUserIdAndStatus(Long userId, String status);
    void deleteByUserId(Long userId);
    Optional<OtpCode> findByUserIdAndStatusAndOperationId(Long id, String status, String operationId);
}