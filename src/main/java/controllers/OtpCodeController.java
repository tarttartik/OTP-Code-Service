package controllers;

import DTOs.ValidationRequest;
import entities.CodeStatus;
import entities.OtpCode;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.OtpService;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpCodeController {
    private OtpService service;
    private static final Logger logger = LogManager.getLogger(OtpCodeController.class);

    @PostMapping("/generate-otp-code")
    public ResponseEntity<OtpCode> generateOtp(@PathVariable String operationId) throws Exception {

        logger.info(STR."Сгенерирован OTP-код для операции \{operationId}");
        return ResponseEntity.ok(service.generateCode(operationId));
    }

    @PostMapping("/validate-otp-code")
    public ResponseEntity<Boolean> validateOtp(@RequestBody ValidationRequest request) throws Exception {
        var storedCode = service.getStoredCode(request.getOperationId());

        if(storedCode.isEmpty()){
            logger.error("{}{}", STR."Не найден OTP код для операции ", request.getOperationId());
            return ResponseEntity.status(404).body(false);
        }

        if(!storedCode.get().getCode().equals(request.getCode())){
            logger.info("Не пройдена валидация кода для операции " + request.getOperationId());
            return ResponseEntity.ok(false);
        }

        storedCode.get().setStatus(CodeStatus.USED);
        logger.info("Успешная валидация кода для операции " + request.getOperationId());
        return ResponseEntity.ok(true);
    }
}
