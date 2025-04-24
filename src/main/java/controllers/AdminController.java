package controllers;

import DTOs.UpdateOtpConfigRequest;
import entities.OtpConfig;
import entities.Role;
import entities.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.OtpService;
import services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    OtpService otpService;
    UserService userService;
    private static final Logger logger = LogManager.getLogger(AdminController.class);

    @PostMapping("/update-otp-config")
    public ResponseEntity<Optional<OtpConfig>> updateOtpConfig(@RequestBody UpdateOtpConfigRequest request) throws Exception {
        var config = otpService.updateOtpConfig(request);
        if(config.isEmpty()){
            logger.error("Не найдена конфигурация для OTP кода");
            return ResponseEntity.status(404).body(config);
        };

        return ResponseEntity.ok(config);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers().stream().filter(u -> u.getRole() != Role.ROLE_ADMIN).collect(Collectors.toList()));
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        boolean isRemoved = userService.deleteUserById(id);

        if (isRemoved) {
            otpService.deleteCodesByUserId(id);
            logger.info(STR."Пользователь с ID \{id} был успешно удален.");
            return ResponseEntity.ok(STR."Пользователь с ID \{id} был успешно удален.");
        } else {
            return ResponseEntity.status(404).body(STR."Пользователь с ID \{id} не найден.");
        }
    }
}
