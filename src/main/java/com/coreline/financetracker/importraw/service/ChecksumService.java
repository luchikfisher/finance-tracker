package com.coreline.financetracker.importraw.service;

import com.coreline.financetracker.common.constants.AppConstants;
import com.coreline.financetracker.common.exception.FinanceTrackerException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.MessageDigest;

@Service
public class ChecksumService {

    public String sha256(InputStream inputStream) {
        try {
            MessageDigest digest = MessageDigest.getInstance(AppConstants.CHECKSUM_ALGORITHM);
            byte[] buffer = new byte[8_192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            byte[] hash = digest.digest();
            return toHex(hash);

        } catch (Exception e) {
            throw new FinanceTrackerException("Failed to calculate checksum", e) {};
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
