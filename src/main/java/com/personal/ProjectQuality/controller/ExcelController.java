package com.personal.ProjectQuality.controller;

import com.google.gson.Gson;
import com.personal.ProjectQuality.model.FileData;
import com.personal.ProjectQuality.model.ValidationResult;
import com.personal.ProjectQuality.service.DatahubService;
import com.personal.ProjectQuality.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/quality")
public class ExcelController {

    @Autowired
    ExcelService excelService;


    @GetMapping("/generateConfigurationFile/")
    public ResponseEntity<byte[]> generateConfigurationFile() throws IOException {
        return excelService.generateConfigurationFile();
    }

    @GetMapping("/generateRulesFile/{tenantCode}")
    public ResponseEntity<byte[]> generateRulesFile(@PathVariable String tenantCode) throws IOException {
        return excelService.generateRulesFile(tenantCode);
    }

    @PostMapping("/generateConfigYaml/{tenantCode}")
    public  ResponseEntity<Resource> generateConfigYaml(@PathVariable String tenantCode,
            @RequestBody FileData path) throws IOException {
        return excelService.generateYaml(path,tenantCode);
    }
}
