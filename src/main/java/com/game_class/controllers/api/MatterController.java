package com.game_class.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game_class.models.Matter;
import com.game_class.repositories.MatterRepository;

@RestController
@RequestMapping("/matters")
public class MatterController {

    @Autowired
    MatterRepository matterRepository;
    
    @GetMapping("/all")
    public List<Matter> getAllMatters() {
        List<Matter> listOfMatters = matterRepository.findAllByOrderByMatterNameAsc();
        return listOfMatters;
    }
}
