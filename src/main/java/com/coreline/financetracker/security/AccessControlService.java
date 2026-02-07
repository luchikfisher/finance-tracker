package com.coreline.financetracker.security;

import com.coreline.financetracker.user.model.AppUser;
import com.coreline.financetracker.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("access")
public class AccessControlService {

    private final UserRepository userRepository;

    public AccessControlService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean canRead(Authentication authentication) {
        AppUser user = resolve(authentication);
        return user.getRole().canRead();
    }

    public boolean canWrite(Authentication authentication) {
        AppUser user = resolve(authentication);
        return user.getRole().canWrite();
    }

    public boolean canDelete(Authentication authentication) {
        AppUser user = resolve(authentication);
        return user.getRole().canDelete();
    }

    public boolean isAdmin(Authentication authentication) {
        AppUser user = resolve(authentication);
        return user.getRole().isAdmin();
    }

    private AppUser resolve(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("No authentication");
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
