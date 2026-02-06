package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.ImportSummaryDto;
import com.coreline.financetracker.api.dto.ImportedFileDto;
import com.coreline.financetracker.api.service.ImportPipelineService;
import com.coreline.financetracker.common.constants.AppConstants;
import com.coreline.financetracker.common.exception.ValidationException;
import com.coreline.financetracker.importraw.repository.ImportedFileRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ImportController {

    private final ImportPipelineService importPipelineService;
    private final ImportedFileRepository importedFileRepository;

    public ImportController(
            ImportPipelineService importPipelineService,
            ImportedFileRepository importedFileRepository
    ) {
        this.importPipelineService = importPipelineService;
        this.importedFileRepository = importedFileRepository;
    }

    @PostMapping(
            path = "/imports",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ImportSummaryDto importFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bankName", defaultValue = AppConstants.DEFAULT_BANK)
            String bankName,
            @RequestParam(value = "export", defaultValue = "false")
            boolean export
    ) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("file is required");
        }

        try {
            return importPipelineService.importAndProcess(
                    bankName,
                    file.getOriginalFilename() == null ? "import.csv" : file.getOriginalFilename(),
                    file.getBytes(),
                    export
            );
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Import pipeline failed", e);
        }
    }

    @GetMapping("/imported-files")
    public List<ImportedFileDto> listImportedFiles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size
    ) {
        PageRequest request = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 200),
                Sort.by(Sort.Direction.DESC, "uploadedAt")
        );
        return importedFileRepository.findAll(request).stream()
                .map(ImportedFileDto::from)
                .toList();
    }
}
