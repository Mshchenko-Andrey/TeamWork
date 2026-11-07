package com.skypro.teamwork.service;

import com.skypro.teamwork.dto.RuleDto;
import com.skypro.teamwork.dto.RuleQueryDto;
import com.skypro.teamwork.entity.RuleEntity;
import com.skypro.teamwork.entity.RuleQueryEntity;
import com.skypro.teamwork.repository.RuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }


    @Transactional
    public RuleDto createRule(RuleDto ruleDto) {
        RuleEntity entity = toEntity(ruleDto);
        RuleEntity savedEntity = ruleRepository.save(entity);
        return toDto(savedEntity);
    }


    @Transactional(readOnly = true)
    public List<RuleDto> getAllRules() {
        return ruleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteRule(UUID productId) {
        ruleRepository.deleteByProductId(productId);
    }



    private RuleDto toDto(RuleEntity entity) {
        RuleDto dto = new RuleDto();
        dto.setId(entity.getId());
        dto.setProductName(entity.getProductName());
        dto.setProductId(entity.getProductId());
        dto.setProductText(entity.getProductText());
        if (entity.getRule() != null) {
            dto.setRule(entity.getRule().stream()
                    .map(this::toQueryDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private RuleQueryDto toQueryDto(RuleQueryEntity entity) {
        RuleQueryDto dto = new RuleQueryDto();
        dto.setQuery(entity.getQuery());
        dto.setArguments(entity.getArguments());
        dto.setNegate(entity.isNegate());
        return dto;
    }

    private RuleEntity toEntity(RuleDto dto) {
        RuleEntity entity = new RuleEntity();
        entity.setProductName(dto.getProductName());
        entity.setProductId(dto.getProductId());
        entity.setProductText(dto.getProductText());
        if (dto.getRule() != null) {
            List<RuleQueryEntity> queries = dto.getRule().stream()
                    .map(this::toQueryEntity)
                    .collect(Collectors.toList());
            entity.setRule(queries);
        }
        return entity;
    }

    private RuleQueryEntity toQueryEntity(RuleQueryDto dto) {
        RuleQueryEntity entity = new RuleQueryEntity();
        entity.setQuery(dto.getQuery());
        entity.setArguments(dto.getArguments());
        entity.setNegate(dto.isNegate());
        return entity;
    }
}