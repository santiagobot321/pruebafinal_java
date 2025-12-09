package com.coopcredit.riskcentralmockservice.controller;

import com.coopcredit.riskcentralmockservice.dto.RiskEvaluationRequest;
import com.coopcredit.riskcentralmockservice.dto.RiskEvaluationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/risk-evaluation")
public class RiskEvaluationController {

    @PostMapping
    public ResponseEntity<RiskEvaluationResponse> evaluateRisk(@RequestBody RiskEvaluationRequest request) {
        // Implementación sugerida:
        // 1. Convertir el documento en un seed numérico (hash mod 1000).
        int seed = Math.abs(request.getDocumento().hashCode()) % 1000;
        Random random = new Random(seed);

        // 2. Generar un score entre 300 y 950 basado en ese seed.
        int score = 300 + random.nextInt(651); // 300 to 950

        // 3. Clasificar:
        String nivelRiesgo;
        String detalle;
        if (score <= 500) {
            nivelRiesgo = "ALTO";
            detalle = "Historial crediticio con alto riesgo de impago.";
        } else if (score <= 700) {
            nivelRiesgo = "MEDIO";
            detalle = "Historial crediticio moderado.";
        } else {
            nivelRiesgo = "BAJO";
            detalle = "Excelente historial crediticio.";
        }

        RiskEvaluationResponse response = new RiskEvaluationResponse(
                request.getDocumento(),
                score,
                nivelRiesgo,
                detalle
        );
        return ResponseEntity.ok(response);
    }
}
