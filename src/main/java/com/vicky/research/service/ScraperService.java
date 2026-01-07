package com.vicky.research.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class ScraperService {

    public String scrapeText(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.body().text();
        } catch (Exception e) {
            throw new RuntimeException("Failed to scrape URL");
        }
    }
}
