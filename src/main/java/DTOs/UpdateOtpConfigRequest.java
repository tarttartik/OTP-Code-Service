package DTOs;

import lombok.Data;

@Data
public class UpdateOtpConfigRequest {
    int codeLength;
    int expirationTime;
}
