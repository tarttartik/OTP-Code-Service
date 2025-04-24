package services;

import DTOs.UpdateOtpConfigRequest;
import entities.CodeStatus;
import entities.OtpCode;
import entities.OtpConfig;
import lombok.RequiredArgsConstructor;
import notification.EmailService;
import notification.FileService;
import notification.SmsService;
import notification.TelegramService;
import org.springframework.stereotype.Service;
import repositories.OtpCodeRepository;
import repositories.OtpConfigRepository;

import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OtpService {
    OtpCodeRepository otpCodeRepository;
    OtpConfigRepository otpConfigRepository;
    UserService userService;
    EmailService emailService;
    SmsService smsService;
    TelegramService telegramService;

    public Optional<OtpConfig> updateOtpConfig(UpdateOtpConfigRequest request) throws Exception {

        var config = getConfig();

        config.setCodeLength(request.getCodeLength());
        config.setExpirationTime(request.getExpirationTime());
        otpConfigRepository.save(config);

        return Optional.of(config);
    }

    public void deleteCodesByUserId(Long id) {
        otpCodeRepository.deleteByUserId(id);
    }

    public OtpCode generateCode(String operationId) throws Exception {

        var config = getConfig();

        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < config.getCodeLength(); i++) {
            int digit = secureRandom.nextInt(10);
            otpBuilder.append(digit);
        }

        int hashModifier = operationId.hashCode() % 10;
        int positionToModify = secureRandom.nextInt(config.getCodeLength());
        otpBuilder.setCharAt(positionToModify, Character.forDigit((otpBuilder.charAt(positionToModify) - '0' + hashModifier) % 10, 10));

        getStoredCode(operationId).ifPresent(otpCode -> otpCodeRepository.delete(otpCode)); //если есть активный код для этой операции, избавляемся от него

        OtpCode code = OtpCode.builder()
                .code(otpBuilder.toString())
                .createdAt(LocalTime.now())
                .operationId(operationId)
                .status(CodeStatus.ACTIVE)
                .userId(userService.getCurrentUser().getId())
                .build();

        otpCodeRepository.save(code);
        notifyUser(code.getCode());

        return code;
    }

    public Optional<OtpCode> getStoredCode(String operationId){

        return otpCodeRepository.findByUserIdAndStatusAndOperationId(
                userService.getCurrentUser().getId(),
                String.valueOf(CodeStatus.ACTIVE),
                operationId);
    }

    public List<OtpCode> getAllCodes(){
        return otpCodeRepository.findAll();
    }

    public OtpConfig getConfig() throws Exception {
        Optional<OtpConfig> configData = otpConfigRepository.findById(1L);
        if(configData.isEmpty()){
            throw new Exception("Конфигурация не найдена");
        }

        return configData.get();
    }

    private void notifyUser(String code){
        var user = userService.getCurrentUser();
        emailService.sendCode(user.getEmail(), code);
        smsService.sendCode(user.getPhoneNumber(), code);
        telegramService.sendCode(user.getUsername(), user.getTgChatId(), code);
        FileService.saveCodeToFile(code, user.getFilePath());
    }
}
