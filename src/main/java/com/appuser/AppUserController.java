package com.appuser;

import com.appuser.login.resetpassword.ResetPasswordRequest;
import com.location.Location;
import com.train.LocationFilteration;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/user")
@CrossOrigin(origins = "*")
public class AppUserController {
    private final AppUserService appUserService;
    LocationFilteration locationFilteration;
    @GetMapping(path ={"/{id}"})
    public AppUser showUserById(@PathVariable Long id){
        return appUserService.findById(id);
    }
    @GetMapping(path = {"/view"})
    public List<AppUser> getUsers() {
        return appUserService.findAll();
    }
    @PutMapping(path ={"/reset-password"})
    public AppUser resetPasswordInDb(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return appUserService.resetPasswordInDb(resetPasswordRequest);
    }
    @PutMapping(path ={"/{id}/update-location"})
    public boolean updateUserLocation(@PathVariable Long id, @RequestBody Location location) {
        appUserService.updateLocation(id, location);
        return locationFilteration.isOutLier(id);
    }
}