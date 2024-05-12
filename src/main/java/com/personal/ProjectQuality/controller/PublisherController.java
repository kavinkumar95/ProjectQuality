package com.personal.ProjectQuality.controller;

import com.google.gson.Gson;
import com.personal.ProjectQuality.model.ValidationResult;
import com.personal.ProjectQuality.service.DatahubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publisher")
public class PublisherController {

    @Autowired
    DatahubService datahubService;

    @PostMapping("/processValidationResult/{dataPlatform}")
    public ResponseEntity<String> processValidationResult(@PathVariable String dataPlatform,@RequestBody String json) {
        Gson gson = new Gson();
        ValidationResult validationResult = gson.fromJson(json, ValidationResult.class);
        datahubService.storeValidationResult(dataPlatform,validationResult);
        datahubService.sendResulttoCatalog(dataPlatform,validationResult);
        return ResponseEntity.ok("Validation result processed successfully");
    }
}
