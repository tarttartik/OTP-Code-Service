import entities.CodeStatus;
import entities.OtpCode;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import services.OtpService;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class CodesLifespanValidator {
    private final ScheduledExecutorService scheduler;
    private final OtpService service;
    private final  Logger  logger;

    public CodesLifespanValidator(){
        this.scheduler = Executors.newScheduledThreadPool(1);
        logger = LogManager.getLogger(CodesLifespanValidator.class);
        service = new OtpService();
    }

    public void startValidation() throws Exception {

        var config = service.getConfig();

        scheduler.scheduleAtFixedRate(() -> {
           for (OtpCode code : service.getAllCodes()) {
               LocalTime expirationTime = code.getCreatedAt().plusSeconds(config.getExpirationTime());

               if(LocalTime.now().isAfter(expirationTime)) code.setStatus(CodeStatus.EXPIRED);
           }
        }, 0, 1, TimeUnit.MINUTES);
    }
}
