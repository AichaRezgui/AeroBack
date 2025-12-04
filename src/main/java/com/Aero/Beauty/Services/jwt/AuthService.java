package com.Aero.Beauty.Services.jwt;

import com.Aero.Beauty.Entities.AppUser;
import com.Aero.Beauty.dto.SignUpRequest;


public interface AuthService {


    AppUser createClient(SignUpRequest signupRequest);

}
