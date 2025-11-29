package com.Aero.Beauty.Repositories;

import com.Aero.Beauty.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
