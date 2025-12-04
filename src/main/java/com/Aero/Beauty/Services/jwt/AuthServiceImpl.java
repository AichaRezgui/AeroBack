package com.Aero.Beauty.Services.jwt;

import com.Aero.Beauty.Entities.Address;
import com.Aero.Beauty.Entities.AppUser;
import com.Aero.Beauty.Repositories.UserRepository;
import com.Aero.Beauty.dto.AddressDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Aero.Beauty.dto.SignUpRequest;

import java.util.ArrayList;
import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser createClient(SignUpRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return null;
        }

        AppUser user = new AppUser();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());

        String hashPassword = passwordEncoder.encode(signupRequest.getMotDePasse());
        user.setPassword(hashPassword);

        List<Address> addresses = new ArrayList<>();
        if (signupRequest.getAddresses() != null) {
            for (AddressDTO dto : signupRequest.getAddresses()) {
                Address addr = new Address();
                addr.setStreet(dto.getStreet());
                addr.setCity(dto.getCity());
                addr.setZip(dto.getZip());
                addr.setCountry(dto.getCountry());
                addr.setUser(user);
                addresses.add(addr);
            }
        }
        user.setAddresses(addresses);

        user.setFavorites(new ArrayList<>());

        user.setOrders(new ArrayList<>());

        return userRepository.save(user);
    }

}

