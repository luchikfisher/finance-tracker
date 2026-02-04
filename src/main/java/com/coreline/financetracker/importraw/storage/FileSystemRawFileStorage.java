package com.coreline.financetracker.importraw.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileSystemRawFileStorage implements RawFileStorage {

    private final Path rootDirectory;

    public FileSystemRawFileStorage(
            @Value("${storage.raw-files.path}") String rootPath
    ) {
        this.rootDirectory = Path.of(rootPath);
    }

    @Override
    public void store(UUID fileId, InputStream inputStream) {
        try {
            Files.createDirectories(rootDirectory);
            Path target = rootDirectory.resolve(fileId.toString());
            Files.copy(inputStream, target);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to store raw file", e);
        }
    }
}
