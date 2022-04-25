package com.appuser.login;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/sign-in")
public class LogInController {
    private final LogInService logInService;
    @PostMapping
    public Long signIn(@RequestBody LogInRequest logInRequest){
        return logInService.signIn(logInRequest);
    }
}