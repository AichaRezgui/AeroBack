package com.Aero.Beauty.Repositories;

import com.Aero.Beauty.Entities.Slider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Long> {
}

