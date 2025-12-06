package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.Order;
import com.Aero.Beauty.Services.OrderService;
import com.Aero.Beauty.dto.OrderDTO;
import com.Aero.Beauty.dto.OrderItemDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @Operation(
            summary = "Lister toutes les commandes",
            description = "Retourne la liste complète des commandes enregistrées dans le système. "
                    + "Principalement utilisé pour l'administration ou l’historique global."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }


    @Operation(
            summary = "Récupérer une commande par ID",
            description = "Retourne les détails d'une commande spécifique en utilisant son identifiant unique."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commande trouvée"),
            @ApiResponse(responseCode = "404", description = "Aucune commande avec cet ID n'a été trouvée")
    })

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(
            summary = "Lister les commandes d'un utilisateur",
            description = "Retourne toutes les commandes passées par un utilisateur spécifique. "
                    + "Utile pour afficher l'historique d'achat."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commandes récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucune commande pour cet utilisateur (optionnel selon ton service)")
    })

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId)
                .stream()
                .map(orderService::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Créer une nouvelle commande",
            description = "Ajoute une commande dans le système. "
                    + "Le corps de la requête doit contenir les produits, les quantités, et l'utilisateur associé."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Commande créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou données manquantes")
    })

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order saved = orderService.saveOrder(order);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Supprimer une commande",
            description = "Supprime une commande existante par son identifiant unique. "
                    + "Opération souvent réservée à l'administration."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Commande supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Commande introuvable")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}

