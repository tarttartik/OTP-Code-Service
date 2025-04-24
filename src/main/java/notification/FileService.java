package notification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public  class FileService {
    private static final Logger logger = LogManager.getLogger(EmailService.class);

    public static void saveCodeToFile(String otpCode, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(otpCode);
        } catch (IOException e) {
            logger.error(STR."Не удалось записать код в файл: \{otpCode}");
            e.printStackTrace();
        }
    }

}
