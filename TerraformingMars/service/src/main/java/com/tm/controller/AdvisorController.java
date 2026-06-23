package com.tm.controller;

import com.tm.dto.AnalysisResponse;
import com.tm.dto.GameStateRequest;
import com.tm.service.AdvisorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AdvisorController {

    private final AdvisorService advisorService;

    public AdvisorController(AdvisorService advisorService) {
        this.advisorService = advisorService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyze(@RequestBody GameStateRequest request) {
        AnalysisResponse analysisResponse = advisorService.analyze(request);
        return ResponseEntity.ok(analysisResponse);
    }
}
