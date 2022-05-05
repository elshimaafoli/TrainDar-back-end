package com.train;

import com.appuser.AppUserRepository;
import com.location.Location;
import com.location.LocationService;
import com.shared_data.UserLocation;
import com.station.StationRepository;
import com.train_station.TrainStation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Service
@AllArgsConstructor
public class TrainService {
    private final TrainRepository trainRepository;
    private final AppUserRepository appUserRepository;
    private final LocationFilteration locationFilteration;
    private final StationRepository stationRepository;
    private final LocationService locationService;
    public Location findById(Long id){
        var savedTrain = trainRepository.findById(id);
        if(savedTrain.isEmpty()){
            throw new IllegalStateException("no such train id");
        }
        return new Location(savedTrain.get().getLocationLat(), savedTrain.get().getLocationLng());
    }

    public void updateLocation(Long id) {
        var existedTrain = trainRepository.findById(id).get();
        Location location = locationFilteration.removeOutliers(existedTrain.getSharedUsers());
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

    public List<UpcomingTrain> getUpcomingTrains(String firstCityName, String secondCityName) {
        var firstCity = stationRepository.findByName(firstCityName);
        var secondCity = stationRepository.findByName(secondCityName);
        List<UpcomingTrain>upcomingTrains = new ArrayList<>();
        //get all trains that pass through firstCity
        TreeSet<Long>trainsTreeSet = new TreeSet<>();
        for(var trainStation : firstCity.getTrains()){
            trainsTreeSet.add(trainStation.getTrain().getId());
        }
        //get all valid trains
        for(var trainStation : secondCity.getTrains()){
            //boolean to check if this train is valid
            var currTrain = trainStation.getTrain();
            //if train not existed in both cities
            if(!trainsTreeSet.contains(currTrain.getId())){
                continue;
            }
            //if train is not active
            if(!locationService.isTrainActive(currTrain)){
                continue;
            }
            String stationDirection = locationService.stationDirection(firstCityName, secondCityName);
            //if train direction not equal to stations direction
            if(!stationDirection.equals(currTrain.getDirection())){
                continue;
            }
            //if curr train has passed the first station
            if(!locationService.hasTrainPassedCity(
                      new Location(currTrain.getLocationLat(), currTrain.getLocationLng())
                    , new Location(firstCity.getLocationLat(), firstCity.getLocationLng())
                    , new Location(secondCity.getLocationLat(), secondCity.getLocationLng())
                    )){
                continue;
            }
            //train is valid
            //add train id and estimated time
            upcomingTrains.add(new UpcomingTrain(currTrain.getId(),
                    //get estimated time between curr train and first city
                    locationService.timeLeft(
                            new Location(currTrain.getLocationLat(), currTrain.getLocationLng())
                            , new Location(firstCity.getLocationLat(), firstCity.getLocationLng())
                    )
                    ));
        }
        return upcomingTrains;
    }
}