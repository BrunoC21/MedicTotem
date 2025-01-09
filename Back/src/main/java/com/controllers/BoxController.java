package com.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.Box;
import com.repository.BoxRepository;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/box")
public class BoxController {

    private BoxRepository boxRepository;

    public BoxController(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    @GetMapping("/box")
    public List<Box> listaTodosMateriales(){
        return boxRepository.findAll();
    }

}
