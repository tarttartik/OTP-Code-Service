package controllers;

import DTOs.JwtAuthenticationResponse;
import DTOs.SignInRequest;
import DTOs.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private AuthService authenticationService;
    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        logger.info("Пользователь {} зарегистрирован", request.getUsername());
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequest request) {
        logger.info("Пользователь {} вошел в систему", request.getUsername());
        return authenticationService.signIn(request);
    }
}