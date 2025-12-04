package com.Aero.Beauty.Services;

import com.Aero.Beauty.Entities.Product;
import com.Aero.Beauty.Repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAllWithImagesAndReviews();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdWithImagesAndReviews(id);
    }
    public List<Product> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrue();
    }

    public List<Product> getNewProducts() {
        return productRepository.findByIsNewTrue();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

}

