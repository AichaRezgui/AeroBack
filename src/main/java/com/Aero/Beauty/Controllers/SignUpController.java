package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.AppUser;
import com.Aero.Beauty.dto.SignUpRequest;
import com.Aero.Beauty.Services.jwt.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class SignUpController {

    private final AuthService authService;

    @Autowired
    public SignUpController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {

        AppUser client = authService.createClient(signUpRequest);

        if (client == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("L'utilisateur existe déjà !");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

}
