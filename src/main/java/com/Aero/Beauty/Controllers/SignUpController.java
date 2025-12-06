package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.AppUser;
import com.Aero.Beauty.dto.SignUpRequest;
import com.Aero.Beauty.Services.jwt.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Créer un nouveau compte utilisateur",
            description = "Enregistre un nouvel utilisateur après la validation des données fournies. "
                    + "Renvoie une erreur si un compte existe déjà avec cet email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "409", description = "L'utilisateur existe déjà"),
            @ApiResponse(responseCode = "400", description = "Données invalides (format incorrect ou champ manquant)")
    })

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
