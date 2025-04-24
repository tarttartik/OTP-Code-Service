package entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@Builder
@Table(name = "otp_codes")
public class OtpCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private CodeStatus status;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalTime createdAt;

    @Column(nullable = false)
    private String operationId;

}
