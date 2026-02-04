package com.coreline.financetracker.orchestration.step;

import com.coreline.financetracker.common.time.ClockProvider;
import com.coreline.financetracker.orchestration.model.*;
import com.coreline.financetracker.orchestration.service.PipelineStep;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrchestrationService {

    private final List<PipelineStep> steps;
    ClockProvider clockProvider;

    public OrchestrationService(List<PipelineStep> steps) {
        this.steps = steps;
    }

    public PipelineResult runPipeline() {
        try {
            for (PipelineStep step : steps) {
                step.execute();
            }
            return new PipelineResult(
                    PipelineStatus.SUCCESS,
                    clockProvider.now(),
                    "Pipeline completed successfully"
            );

        } catch (Exception e) {
            return new PipelineResult(
                    PipelineStatus.FAILED,
                    clockProvider.now(),
                    "Pipeline failed: " + e.getMessage()
            );
        }
    }
}
