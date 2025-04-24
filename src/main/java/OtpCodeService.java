import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OtpCodeService {
    public static void main(String[] args) throws Exception {

        var validator = new CodesLifespanValidator();
        validator.startValidation();

        SpringApplication.run(OtpCodeService.class, args);
    }
}
