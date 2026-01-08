package com.vicky.research.controller;

import com.vicky.research.service.ScraperService;
import com.vicky.research.service.PdfTextExtractorService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final PdfTextExtractorService pdfTextExtractorService;

    public SummarizeController(
            ScraperService scraperService,
            SummarizerService summarizerService,
            PdfTextExtractorService pdfTextExtractorService
    ) {
        this.scraperService = scraperService;
        this.summarizerService = summarizerService;
        this.pdfTextExtractorService = pdfTextExtractorService;
    }
    
    @GetMapping("/api/summarize")
    public String summarize(@RequestParam String url) {

        String rawText = scraperService.scrapeText(url);
        return summarizerService.summarize(rawText);
    }
    
    @PostMapping("/api/summarize-pdf")
    public String summarizePdf(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("Uploaded PDF is empty");
        }

        String extractedText = pdfTextExtractorService.extractText(file);
        return summarizerService.summarize(extractedText);
    }

    
}
