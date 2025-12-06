package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.AppUser;
import com.Aero.Beauty.dto.LoginRequest;
import com.Aero.Beauty.dto.LoginResponse;
import com.Aero.Beauty.Services.jwt.UserServiceImplement;
import com.Aero.Beauty.utils.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImplement userServiceImplement;
    private final JwtUtil jwtUtil;

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${app.cookie.sameSite:None}")
    private String sameSite;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager,
                           UserServiceImplement userServiceImplement,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userServiceImplement = userServiceImplement;
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "Authentifier un utilisateur",
            description = "Vérifie l'email et le mot de passe fournis. "
                    + "Si l'authentification réussie, un jeton JWT est généré et envoyé sous forme de cookie HTTPOnly "
                    + "pour sécuriser la session."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentification réussie, JWT généré"),
            @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect"),
            @ApiResponse(responseCode = "400", description = "Requête mal formée")
    })

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getMotDePasse()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email ou mot de passe incorrect.");
        }

        UserDetails userDetails = userServiceImplement.loadUserByUsername(loginRequest.getEmail());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());
        Long userId = userServiceImplement.getIdUser(loginRequest.getEmail());

        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite(sameSite)
                .maxAge(24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new LoginResponse(userId));
    }

    @Operation(
            summary = "Déconnecter l'utilisateur",
            description = "Supprime le cookie JWT en le remplaçant par un cookie vide avec une expiration immédiate. "
                    + "Cela met fin à la session utilisateur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Déconnexion réussie, cookie supprimé")
    })

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite(sameSite)
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Obtenir les informations de l'utilisateur connecté",
            description = "Récupère les données de l'utilisateur actuellement authentifié en lisant le JWT stocké dans le cookie. "
                    + "Retourne l'ID et l'email de l'utilisateur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Informations récupérées avec succès"),
            @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié ou token invalide")
    })

    @GetMapping("/me")
    public ResponseEntity<?> me(@CookieValue(name = "jwt", required = false) String token) {

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body("Non authentifié");
        }

        String email;
        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token invalide");
        }

        AppUser user = userServiceImplement.getUserByEmail(email);
        return ResponseEntity.ok(
                Map.of(
                        "id", user.getId(),
                        "email", user.getEmail()
                )
        );
    }


}
