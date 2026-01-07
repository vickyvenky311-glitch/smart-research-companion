package com.vicky.research.controller;

import com.vicky.research.service.ScraperService;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.vicky.research.service.SummarizerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class SummarizeController {

    private final ScraperService scraperService;
    private final SummarizerService summarizerService;

    public SummarizeController(
            ScraperService scraperService,
            SummarizerService summarizerService
    ) {
        this.scraperService = scraperService;
        this.summarizerService = summarizerService;
    }

    @GetMapping("/api/summarize")
    public String summarize(@RequestParam String url) {

        String rawText = scraperService.scrapeText(url);
        return summarizerService.summarize(rawText);
    }
}
