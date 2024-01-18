package com.personal.ProjectQuality.controller;

import com.personal.ProjectQuality.model.FileData;
import com.personal.ProjectQuality.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/quality")
public class ProductController {

    @Autowired
    ExcelService excelService;


    @GetMapping("/generateConfigurationFile/")
    public ResponseEntity<byte[]> generateConfigurationFile() throws IOException {
        return excelService.generateConfigurationFile();
    }

    @GetMapping("/generateRulesFile/")
    public ResponseEntity<byte[]> generateRulesFile() throws IOException {
        return excelService.generateRulesFile();
    }

    @PostMapping("/generateConfigYaml/")
    public  ResponseEntity<Resource> generateConfigYaml(@RequestBody FileData path) throws IOException {
        return excelService.generateYaml(path);
    }
}
