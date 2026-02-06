package com.coreline.financetracker.config;

import com.coreline.financetracker.enrichment.model.Category;
import com.coreline.financetracker.enrichment.rule.EnrichmentRule;
import com.coreline.financetracker.enrichment.rule.KeywordCategoryRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public EnrichmentRule keywordCategoryRule() {
        Map<String, Category> keywords = new LinkedHashMap<>();
        keywords.put("salary", Category.SALARY);
        keywords.put("freelance", Category.SALARY);
        keywords.put("rent", Category.RENT);
        keywords.put("grocery", Category.GROCERIES);
        keywords.put("supermarket", Category.GROCERIES);
        keywords.put("uber", Category.TRANSPORT);
        keywords.put("rideshare", Category.TRANSPORT);
        keywords.put("fuel", Category.TRANSPORT);
        keywords.put("coffee", Category.ENTERTAINMENT);
        keywords.put("restaurant", Category.ENTERTAINMENT);
        keywords.put("movie", Category.ENTERTAINMENT);
        keywords.put("netflix", Category.ENTERTAINMENT);
        keywords.put("utility", Category.UTILITIES);
        keywords.put("electric", Category.UTILITIES);
        keywords.put("gas", Category.UTILITIES);
        keywords.put("pharmacy", Category.HEALTH);
        keywords.put("doctor", Category.HEALTH);
        keywords.put("gym", Category.HEALTH);
        return new KeywordCategoryRule(keywords);
    }
}
