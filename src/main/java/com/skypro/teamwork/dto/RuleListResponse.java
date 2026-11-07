package com.skypro.teamwork.dto;

import java.util.List;

public class RuleListResponse {
    private List<RuleDto> data;

    public RuleListResponse(List<RuleDto> data) {
        this.data = data;
    }

    public List<RuleDto> getData() { return data; }
    public void setData(List<RuleDto> data) { this.data = data; }
}