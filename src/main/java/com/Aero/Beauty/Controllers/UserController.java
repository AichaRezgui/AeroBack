package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.AppUser;
import com.Aero.Beauty.Entities.Product;
import com.Aero.Beauty.Entities.Order;
import com.Aero.Beauty.Services.UserService;
import com.Aero.Beauty.dto.UpdateUserRequest;
import com.Aero.Beauty.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Lister tous les utilisateurs",
            description = "Retourne la liste complète des utilisateurs enregistrés dans le système. "
                    + "Peut être utilisé pour un tableau de bord administrateur ou la gestion des comptes."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })

    @GetMapping
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Récupérer un utilisateur par ID",
            description = "Retourne les informations détaillées d’un utilisateur spécifique grâce à son identifiant unique. "
                    + "Cet endpoint est généralement utilisé pour afficher le profil ou vérifier l’existence d’un compte."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Aucun utilisateur avec cet ID n’a été trouvé"),
            @ApiResponse(responseCode = "400", description = "ID invalide")
    })

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



    @Operation(
            summary = "Trouver un utilisateur par email",
            description = "Recherche un utilisateur à partir de son adresse email. "
                    + "Souvent utilisé pour l’authentification, la récupération de mot de passe ou la vérification d’existence."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Aucun utilisateur avec cet email n’existe"),
            @ApiResponse(responseCode = "400", description = "Format d'email invalide")
    })

    @GetMapping("/email/{email}")
    public ResponseEntity<AppUser> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @Operation(
            summary = "Mettre à jour un utilisateur",
            description = "Permet de modifier les informations d’un utilisateur existant en fournissant son ID. "
                    + "Les champs non fournis restent inchangés."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides envoyées dans la requête")
    })

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


    @Operation(
            summary = "Supprimer un utilisateur",
            description = "Supprime définitivement un utilisateur à partir de son identifiant. "
                    + "Cette action est irréversible et nécessite que l’utilisateur existe."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable"),
            @ApiResponse(responseCode = "400", description = "ID invalide")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

