package com.vicky.research.scheduler;

import com.vicky.research.service.ScraperService;
import com.vicky.research.service.SummarizerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ResearchScheduler {

    private final ScraperService scraperService;
    private final SummarizerService summarizerService;

    public ResearchScheduler(
            ScraperService scraperService,
            SummarizerService summarizerService
    ) {
        this.scraperService = scraperService;
        this.summarizerService = summarizerService;
    }

    // Runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void autoSummarize() {

        String url = "https://example.com";

        System.out.println("🔄 Scheduler triggered...");
        String content = scraperService.scrapeText(url);
        String summary = summarizerService.summarize(content);

        System.out.println("🧠 Auto Summary:");
        System.out.println(summary);
        System.out.println("--------------------------------------------------");
    }
}
