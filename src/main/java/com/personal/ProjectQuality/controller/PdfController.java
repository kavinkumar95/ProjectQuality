package com.personal.ProjectQuality.controller;

import com.personal.ProjectQuality.service.ExcelService;
import com.personal.ProjectQuality.service.PdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/generate")
public class PdfController {

    @Autowired
    PdfService pdfService;
    @GetMapping(value = "/pdf/{datasource}/{schema}/{tableName}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String datasource,@PathVariable String schema,@PathVariable String tableName) throws IOException {
        return pdfService.generatePdf(datasource,schema,tableName);

    }

}
