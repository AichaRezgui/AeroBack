package com.Aero.Beauty.Services;

import com.Aero.Beauty.Entities.Slider;
import com.Aero.Beauty.Repositories.ProductRepository;
import com.Aero.Beauty.Repositories.SliderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SliderServiceImpl implements SliderService {

    @Autowired
    private SliderRepository sliderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Slider> getAll() {
        return sliderRepository.findAll();
    }

    @Override
    public Slider getById(Long id) {
        return sliderRepository.findById(id).orElse(null);
    }

    @Override
    public Slider save(Slider slider) {
        return sliderRepository.save(slider);
    }

    @Override
    public void delete(Long id) {
        sliderRepository.deleteById(id);
    }
}

