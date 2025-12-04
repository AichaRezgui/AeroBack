package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.Slider;
import com.Aero.Beauty.Repositories.ProductRepository;
import com.Aero.Beauty.Services.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sliders")
@CrossOrigin("*")
public class SliderController {

    @Autowired
    private SliderService sliderService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Slider> getAll() {
        return sliderService.getAll();
    }

    @GetMapping("/{id}")
    public Slider getById(@PathVariable Long id) {
        return sliderService.getById(id);
    }

    @PostMapping
    public Slider create(@RequestBody Slider slider) {
        return sliderService.save(slider);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sliderService.delete(id);
    }
}

