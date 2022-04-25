package com.appuser.login;

import com.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogInService {
    private final AppUserService appUserService;

    public Long signIn(LogInRequest logInRequest) {
        return appUserService.signInUser(
            logInRequest
        );
    }
}
