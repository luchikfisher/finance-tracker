package com.coreline.financetracker.orchestration.runner;

import com.coreline.financetracker.orchestration.model.PipelineResult;
import com.coreline.financetracker.orchestration.model.PipelineStatus;
import com.coreline.financetracker.orchestration.step.OrchestrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FinanceTrackerRunner implements ApplicationRunner {

    private final OrchestrationService orchestrationService;
    private final boolean autoRun;

    public FinanceTrackerRunner(
            OrchestrationService orchestrationService,
            @Value("${pipeline.auto-run:true}") boolean autoRun
    ) {
        this.orchestrationService = orchestrationService;
        this.autoRun = autoRun;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!autoRun) {
            System.out.println("Pipeline auto-run disabled");
            return;
        }

        System.out.println("======================================");
        System.out.println(" Finance Tracker – Pipeline Starting ");
        System.out.println("======================================");

        PipelineResult result = orchestrationService.runPipeline();

        if (result.status() == PipelineStatus.SUCCESS) {
            System.out.println("✔ Pipeline finished successfully");
        } else {
            System.err.println("✖ Pipeline failed");
            System.err.println(result.message());
        }

        System.out.println("Finished at: " + result.finishedAt());
        System.out.println("======================================");
    }
}
