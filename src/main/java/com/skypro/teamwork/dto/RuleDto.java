package com.skypro.teamwork.dto;

import java.util.List;
import java.util.UUID;

public class RuleDto {
    private Long id;
    private String productName;
    private UUID productId;
    private String productText;
    private List<RuleQueryDto> rule;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public List<RuleQueryDto> getRule() { return rule; }
    public void setRule(List<RuleQueryDto> rule) { this.rule = rule; }
}