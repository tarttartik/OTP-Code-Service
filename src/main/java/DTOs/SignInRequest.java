package DTOs;
import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}

