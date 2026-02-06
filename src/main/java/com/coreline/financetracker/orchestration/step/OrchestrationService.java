package com.coreline.financetracker.orchestration.step;

import com.coreline.financetracker.common.time.ClockProvider;
import com.coreline.financetracker.orchestration.model.*;
import com.coreline.financetracker.orchestration.service.PipelineStep;
import com.coreline.financetracker.orchestration.service.PipelineContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrchestrationService {

    private final List<PipelineStep> steps;
    private final ClockProvider clockProvider;

    public OrchestrationService(
            List<PipelineStep> steps,
            ClockProvider clockProvider
    ) {
        this.steps = steps;
        this.clockProvider = clockProvider;
    }

    public PipelineResult runPipeline() {
        PipelineContext context = new PipelineContext();
        try {
            for (PipelineStep step : steps) {
                step.execute(context);
            }
            return new PipelineResult(
                    PipelineStatus.SUCCESS,
                    clockProvider.now(),
                    "Pipeline completed successfully"
            );

        } catch (Exception e) {
            String detail = e.getMessage();
            if (detail == null || detail.isBlank()) {
                detail = e.getClass().getSimpleName();
            }
            return new PipelineResult(
                    PipelineStatus.FAILED,
                    clockProvider.now(),
                    "Pipeline failed: " + detail
            );
        }
    }
}
