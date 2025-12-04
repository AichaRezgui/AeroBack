package com.Aero.Beauty.Services;

import com.Aero.Beauty.Entities.Slider;

import java.util.List;

public interface SliderService {
    List<Slider> getAll();
    Slider getById(Long id);
    Slider save(Slider slider);
    void delete(Long id);
}
