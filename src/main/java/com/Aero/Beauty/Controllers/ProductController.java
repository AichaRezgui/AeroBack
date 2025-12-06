package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.Product;
import com.Aero.Beauty.Services.ProductService;
import com.Aero.Beauty.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(
            summary = "Lister tous les produits",
            description = "Retourne une liste complète de tous les produits disponibles dans le catalogue. "
                    + "Utilisé pour afficher la boutique ou un tableau de gestion."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des produits récupérée")
    })

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice());
                    dto.setRating(product.getRating());
                    dto.setStock(product.getStock());
                    dto.setIsFeatured(product.getFeatured());
                    dto.setIsNew(product.getNew());
                    dto.setCategoryId(product.getCategory().getId());
                    dto.setImages(product.getImages());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Operation(
            summary = "Récupérer un produit par ID",
            description = "Permet de récupérer les détails d’un produit en utilisant son identifiant unique. "
                    + "Idéal pour afficher les détails du produit dans la boutique."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit trouvé"),
            @ApiResponse(responseCode = "404", description = "Produit introuvable"),
            @ApiResponse(responseCode = "400", description = "ID invalide")
    })

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice());
                    dto.setRating(product.getRating());
                    dto.setStock(product.getStock());
                    dto.setIsFeatured(product.getFeatured());
                    dto.setIsNew(product.getNew());
                    dto.setCategoryId(product.getCategory().getId());
                    dto.setImages(product.getImages());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(
            summary = "Produits en vedette",
            description = "Retourne la liste des produits mis en avant (produits populaires ou sélectionnés)."
    )
    @GetMapping("/featured")
    public List<Product> getFeaturedProducts() {
        return productService.getFeaturedProducts();
    }

    @Operation(
            summary = "Nouveaux produits",
            description = "Retourne la liste des derniers produits ajoutés au catalogue."
    )
    @GetMapping("/new")
    public List<Product> getNewProducts() {
        return productService.getNewProducts();
    }


    @Operation(
            summary = "Ajouter un nouveau produit",
            description = "Crée un produit dans la base de données après validation des données fournies. "
                    + "Utilisé pour enrichir le catalogue d’un e-commerce ou d’un back-office."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produit créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (prix, stock, nom manquant, etc.)")
    })

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }


    @Operation(
            summary = "Mettre à jour un produit existant",
            description = "Modifie les informations d’un produit existant. "
                    + "Le produit doit exister et les données envoyées doivent être valides."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Produit introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.getProductById(id)
                .map(existing -> {
                    product.setId(id);
                    return ResponseEntity.ok(productService.saveProduct(product));
                }).orElse(ResponseEntity.notFound().build());
    }


    @Operation(
            summary = "Supprimer un produit",
            description = "Supprime définitivement un produit du catalogue. "
                    + "Action généralement réservée à l’administrateur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produit supprimé"),
            @ApiResponse(responseCode = "404", description = "Produit introuvable")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtenir les produits d'une catégorie",
            description = "Retourne la liste de tous les produits associés à la catégorie indiquée."
    )
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

}

