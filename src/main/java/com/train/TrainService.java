package com.train;

import com.appuser.AppUserRepository;
import com.location.Location;
import com.shared_data.UserLocation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TrainService {
    private final TrainRepository trainRepository;
    private final AppUserRepository appUserRepository;
    private final LocationFilteration locationFilteration;
    public Location findById(Long id){
        var savedTrain = trainRepository.findById(id);
        if(savedTrain.isEmpty()){
            throw new IllegalStateException("no such train id");
        }
        return new Location(savedTrain.get().getLocationLat(), savedTrain.get().getLocationLng());
    }

    public void updateLocation(Long id) {
        List<UserLocation> userLocations = new ArrayList<>();
        var existedTrain = trainRepository.findById(id).get();
        for(var user : existedTrain.getSharedUsers()){
            userLocations.add(new UserLocation(user.getId(), new Location(user.getLocationLat(), user.getLocationLng())));
        }
        Location location = locationFilteration.removeOutliers(userLocations);
        if(!location.getLocationLat().equals(BigDecimal.valueOf(-1)) && !location.getLocationLng().equals(BigDecimal.valueOf(-1))){
            existedTrain.setLocationLat(location.getLocationLat());
            existedTrain.setLocationLng(location.getLocationLng());
            existedTrain.setLastKnownTime(LocalDateTime.now());
            trainRepository.saveAndFlush(existedTrain);
        }
    }

    public void deleteUSer(Long trainId, Long userId) {
        var existedUSer = appUserRepository.findById(userId);
        var existedTrain = trainRepository.findById(trainId);
        if (existedUSer.isEmpty() || existedTrain.isEmpty()) {
            throw new IllegalStateException("can't delete user from train invalid train-id or user-id");
        }
        existedTrain.get().getSharedUsers().remove(existedUSer.get());
        existedUSer.get().setTrain(null);
        trainRepository.saveAndFlush(existedTrain.get());
    }

    public void addUser(Long trainId, Long userId) {
        var existedUSer = appUserRepository.findById(userId);
        var existedTrain = trainRepository.findById(trainId);
        if (existedUSer.isEmpty() || existedTrain.isEmpty()) {
            throw new IllegalStateException("can't add user to train invalid train-id or user-id");
        }
        var userOldTrain = existedUSer.get().getTrain();
        if (userOldTrain != null) {
            deleteUSer(userOldTrain.getId(), existedUSer.get().getId());
        }
        existedUSer.get().setTrain(existedTrain.get());
        existedTrain.get().getSharedUsers().add(existedUSer.get());
        trainRepository.saveAndFlush(existedTrain.get());
    }
}