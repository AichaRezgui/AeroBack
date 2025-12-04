package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.dto.LoginRequest;
import com.Aero.Beauty.dto.LoginResponse;
import com.Aero.Beauty.Services.jwt.UserServiceImplement;
import com.Aero.Beauty.utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImplement userServiceImplement;
    private final JwtUtil jwtUtil;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager,
                           UserServiceImplement userServiceImplement,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userServiceImplement = userServiceImplement;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest,
                               HttpServletResponse response) throws Exception {

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

        return new LoginResponse(jwt, userServiceImplement.getIdUser(loginRequest.getEmail()));
    }
}
