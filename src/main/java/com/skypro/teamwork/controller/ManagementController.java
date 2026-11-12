package com.skypro.teamwork.controller;

import com.skypro.teamwork.repository.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final RecommendationsRepository recommendationsRepository;
    private final BuildProperties buildProperties;

    public ManagementController(RecommendationsRepository recommendationsRepository,
                                BuildProperties buildProperties) {
        this.recommendationsRepository = recommendationsRepository;
        this.buildProperties = buildProperties;
    }


    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        recommendationsRepository.clearAllCaches();
        return ResponseEntity.ok().build();
    }


    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        return ResponseEntity.ok(Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        ));
    }
}