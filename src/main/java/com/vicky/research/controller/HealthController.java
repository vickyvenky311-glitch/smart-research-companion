package com.vicky.research.controller;

import com.vicky.research.service.ScraperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final ScraperService scraperService;

    public HealthController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/api/scrape")
    public String scrape(@RequestParam String url) {
        return scraperService.scrapeText(url);
    }
}
