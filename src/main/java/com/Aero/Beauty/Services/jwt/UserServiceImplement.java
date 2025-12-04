package com.Aero.Beauty.Services.jwt;


import java.util.Collections;
import java.util.List;

import java.util.Optional;

import com.Aero.Beauty.Entities.AppUser;
import com.Aero.Beauty.Repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;




@Service
public class UserServiceImplement implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email non trouvé : " + email));

        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT"));

        return new User(user.getEmail(), user.getPassword(), authorities);
    }

    public Long getIdUser(String email) {
        return userRepository.findByEmail(email)
                .map(AppUser::getId)
                .orElseThrow(() -> new UsernameNotFoundException("Email non trouvé : " + email));
    }
}

