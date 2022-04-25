package com.appuser.login.resetpassword;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/reset-password")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ResetPasswordController {
    ResetPasswordService resetPasswordService;
    @GetMapping(path= "/email")
    public String validEmail(@RequestParam("email") String email){
        return (resetPasswordService.validEmail(email));
    }

    @GetMapping(path= "/email-token")
    public String validEmailToken(@RequestParam("email") String email, @RequestParam("token") String token){
        return (resetPasswordService.validEmailToken(email, token));
    }

    @PostMapping(path= "/email-password")
    public String validEmailPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return (resetPasswordService.validEmailPassword(resetPasswordRequest));
    }
}