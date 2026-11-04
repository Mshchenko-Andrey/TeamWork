package com.skypro.teamwork.dto;

import java.util.UUID;

public class RecommendationDto {
    private UUID id;
    private String name;
    private String text;


    public RecommendationDto() {
    }

    public RecommendationDto(UUID id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }


    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getText() { return text; }


    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setText(String text) { this.text = text; }
}