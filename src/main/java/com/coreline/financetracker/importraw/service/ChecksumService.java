package com.coreline.financetracker.importraw.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.MessageDigest;

@Service
public class ChecksumService {

    public String sha256(InputStream inputStream) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8_192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            byte[] hash = digest.digest();
            return toHex(hash);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to calculate checksum", e);
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
