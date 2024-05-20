package io.abun.wmb.Auth;

import io.abun.wmb.Auth.dto.AuthRequest;
import io.abun.wmb.Auth.dto.LoginResponse;
import io.abun.wmb.Auth.dto.RegisterResponse;
import io.abun.wmb.Auth.interfaces.AuthService;
import io.abun.wmb.Constants.CommonResponse;
import io.abun.wmb.Constants.Routes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Routes.AUTH)
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<CommonResponse<?>> registerUser(@RequestBody AuthRequest request) {
        RegisterResponse register = authService.register(request);

        CommonResponse<RegisterResponse> response = new CommonResponse<>(
                HttpStatus.CREATED.value(),
                "Registration success",
                register,
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<CommonResponse<?>> loginUser(@RequestBody AuthRequest request) {
        LoginResponse loginResponse = authService.login(request);

        CommonResponse<LoginResponse> response = new CommonResponse<>(
                HttpStatus.OK.value(),
                "Login success",
                loginResponse,
                null
        );

        return ResponseEntity.ok(response);
    }
}

