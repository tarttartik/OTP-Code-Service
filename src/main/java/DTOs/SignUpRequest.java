package DTOs;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String tgChatId;
    private String filePath;
}
