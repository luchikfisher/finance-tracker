package com.coreline.financetracker.importraw.storage;

import java.io.InputStream;
import java.util.UUID;

public interface RawFileStorage {

    void store(UUID fileId, InputStream inputStream);
}
