package com.appuser;

import com.appuser.login.LogInRequest;
import com.location.Location;
import com.points.PointsHistoryService;
import com.shared_data.token.ConfirmationToken;
import com.shared_data.token.ConfirmationTokenService;
import com.appuser.login.resetpassword.ResetPasswordRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final PointsHistoryService pointsHistoryService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }
    public String signUpUser(AppUser appUser){
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if(userExists){

            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email but make sure that the last confirmation email was expired.

            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        pointsHistoryService.points(appUser.getId(),"Registeration");
        appUserRepository.save(appUser);

        String token= UUID.randomUUID().toString();
        ConfirmationToken confirmationToken=new ConfirmationToken(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        //TODO : SEND EMAIL
        return token;
        //return "REGISTRATION COMPLETED";
    }
    public String resetPassword(String email) {
        var savedUser = appUserRepository.findByEmail(email);
        if (savedUser.isEmpty()) {
            throw new IllegalStateException("Email doesn't exist");
        } else {
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), savedUser.get());
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            return token;
        }
    }
    public Long signInUser(LogInRequest logInRequest){
        var savedUser = appUserRepository.findByEmail(logInRequest.getEmail());
        boolean userExists = savedUser.isPresent();
        if(!userExists){
            // TODO if email not confirmed send confirmation email.

            throw new IllegalStateException("Email doesn't exist");
        }
        if (!bCryptPasswordEncoder.matches(logInRequest.getPassword(),savedUser.get().getPassword())){
            throw new IllegalStateException("Wrong password");
        }

        return savedUser.get().getId();
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public AppUser findById(Long id) {
        boolean userExists = appUserRepository.findById(id).isPresent();
        if(!userExists){
            throw new IllegalStateException("no such id");
        }
        else {
            return appUserRepository.findById(id).get();
        }
    }

    public AppUser resetPasswordInDb(ResetPasswordRequest resetPasswordRequest) {
        var existedUser = appUserRepository.findByEmail(resetPasswordRequest.getEmail());
        if(existedUser.isEmpty()){
            throw new IllegalStateException("no such email");
        }
        else{
            String encodedPassword = bCryptPasswordEncoder.encode(resetPasswordRequest.getPassword());
            existedUser.get().setPassword(encodedPassword);
            appUserRepository.saveAndFlush(existedUser.get());
            return existedUser.get();
        }
    }

    public void updateLocation(long id, Location location) {
        boolean userExists = appUserRepository.findById(id).isPresent();
        if(!userExists){
            throw new IllegalStateException("no such id");
        }
        else {
            appUserRepository.updateLocation(id, location.getLocationLat(), location.getLocationLng());
            // TODO: app user not updated directly
        }
    }

    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }
}