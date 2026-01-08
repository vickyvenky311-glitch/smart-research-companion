package com.vicky.research.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class PdfTextExtractorService {

	public String extractText(MultipartFile file) {
	    try (InputStream inputStream = file.getInputStream();
	         PDDocument document = PDDocument.load(inputStream)) {

	        PDFTextStripper stripper = new PDFTextStripper();
	        String text = stripper.getText(document);

	        if (text == null || text.trim().isEmpty()) {
	            throw new RuntimeException("PDF contains no extractable text");
	        }

	        // 🔒 SANITIZE text for JSON / OpenAI
	        text = text
	                .replaceAll("\\p{C}", " ")   // remove control chars
	                .replaceAll("\\s+", " ")     // normalize spaces
	                .trim();

	        // 🔒 TOKEN SAFETY
	        if (text.length() > 6000) {
	            text = text.substring(0, 6000);
	        }

	        return text;

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to extract text from PDF", e);
	    }
	}
    }