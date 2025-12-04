package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.AppUser;
import com.Aero.Beauty.Entities.Product;
import com.Aero.Beauty.Entities.Order;
import com.Aero.Beauty.Services.UserService;
import com.Aero.Beauty.dto.UpdateUserRequest;
import com.Aero.Beauty.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    dto.setEmail(user.getEmail());
                    dto.setAddresses(user.getAddresses());
                    dto.setFavorites(user.getFavorites().stream().map(Product::getId).toList());
                    dto.setOrders(user.getOrders().stream().map(Order::getId).toList());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<AppUser> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AppUser createUser(@RequestBody AppUser user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return userService.getUserById(id)
                .map(existing -> {
                    if (request.getCurrentPassword() != null && !request.getCurrentPassword().isEmpty()) {
                        if (!passwordEncoder.matches(request.getCurrentPassword(), existing.getPassword())) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("Ancien mot de passe incorrect !");
                        }
                        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
                            existing.setPassword(passwordEncoder.encode(request.getNewPassword()));
                        }
                    }
                    existing.setFirstName(request.getFirstName());
                    existing.setLastName(request.getLastName());
                    existing.setEmail(request.getEmail());

                    AppUser updated = userService.saveUser(existing);
                    return ResponseEntity.ok(updated);
                }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

