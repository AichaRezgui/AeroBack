package com.Aero.Beauty.Repositories;

import com.Aero.Beauty.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.images " +
            "LEFT JOIN FETCH p.reviews")
    List<Product> findAllWithImagesAndReviews();


    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images LEFT JOIN FETCH p.reviews WHERE p.id = :id")
    Optional<Product> findByIdWithImagesAndReviews(@Param("id") Long id);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByIsFeaturedTrue();
    List<Product> findByIsNewTrue();
}