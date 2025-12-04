package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.Product;
import com.Aero.Beauty.Services.ProductService;
import com.Aero.Beauty.dto.ProductDTO;
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


    @GetMapping("/featured")
    public List<Product> getFeaturedProducts() {
        return productService.getFeaturedProducts();
    }

    @GetMapping("/new")
    public List<Product> getNewProducts() {
        return productService.getNewProducts();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.getProductById(id)
                .map(existing -> {
                    product.setId(id);
                    return ResponseEntity.ok(productService.saveProduct(product));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

}

