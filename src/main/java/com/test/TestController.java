package com.test;

import com.appuser.AppUser;
import com.appuser.AppUserRepository;
import com.location.Location;
import com.location.LocationService;
import com.points.PointsHistoryService;
import com.shared_data.path.PathPoints;
import com.shared_data.path.PathPointsRepository;
import com.station.NearestStation;
import com.station.Station;
import com.station.StationRepository;
import com.station.StationService;
import com.train.TrainService;
import com.train.Train;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/test")
public class TestController {
    public PointsHistoryService pointsHistoryService;
    public AppUserRepository appUserRepository;
    public PathPointsRepository pathPointsRepository;
    LocationService locationService;
    TrainService trainService;
    StationService stationService;
    StationRepository stationRepository;
    List<Boolean> returnResults = new ArrayList<>();

    @GetMapping
    public List<Boolean> runTest(){
        returnResults.clear();
        returnResults.add(locationService.isPassThrough(3, 1));
        returnResults.add(locationService.isPassThrough(3, 2));
        returnResults.add(locationService.isPassThrough(3, 5));
        return returnResults;
    }

    //tested
    @GetMapping(path = {"/pathPointsTest"})
    public List<PathPoints> pathPointsTest(){
        return pathPointsRepository.findAll();
    }

    //tested
    @GetMapping(path = {"/pathPointsByLatTest"})
    public List<PathPoints> pathPointsByLatTest(){
        return pathPointsRepository.getAllPointsByLat();
    }

    //tested
    @GetMapping(path = {"/pathPointsByLngTest"})
    public List<PathPoints> pathPointsByLngTest(){
        return pathPointsRepository.getAllPointsByLng();
    }

    //tested
    @GetMapping(path = {"/pathPointsIDTest"})
    public int pathPointsIDTest(){
        return pathPointsRepository.getIDByLatLng(BigDecimal.valueOf(26.551245), BigDecimal.valueOf(31.699417));
    }

    //tested
    @GetMapping(path = {"/timeLeftTest"})
    public double timeLeftTest(){
        Location location1=new Location(BigDecimal.valueOf(27.181265),BigDecimal.valueOf(31.187582)),
                location2=new Location(BigDecimal.valueOf(28.18112),BigDecimal.valueOf(31.187607));
        return locationService.timeLeft(location1,location2);
    }

    //tested
    @GetMapping(path = {"/stationDirection/{n1}-{n2}"})
    public String stationDirectionTest(@PathVariable String n1,@PathVariable String n2){
        Station station1=stationRepository.findByName(n1),station2=stationRepository.findByName(n2);
        Location location1=new Location(station1.getLocationLat(),station1.getLocationLng()),
                location2=new Location(station2.getLocationLat(),station2.getLocationLng());
        return locationService.DirectionUpDown(location1,location2);
    }

    //tested
    @GetMapping(path = {"/DirectionUpDown/{lat1}-{lng1}-{lat2}-{lng2}"})
    public String DirectionUpDownTest(@PathVariable BigDecimal lat1, @PathVariable BigDecimal lng1, @PathVariable BigDecimal lat2, @PathVariable BigDecimal lng2)
    {
        Location location1=new Location(lat1,lng1),
                location2 =new Location(lat2,lng2);
        return locationService.DirectionUpDown(location1,location2);
    }

    //hard test
    @GetMapping(path = {"/D"})
    public String D()
    {
        Location location1=new Location(BigDecimal.valueOf(26.55654923372615),BigDecimal.valueOf(31.693084504454426)),
                location2 =new Location(BigDecimal.valueOf(26.557952111875835),BigDecimal.valueOf(31.69158138626245));
        return locationService.DirectionUpDown(location1,location2);
    }

    //tested
    @GetMapping(path = {"/Closest/{lat1}-{lng1}"})
    public int c(@PathVariable BigDecimal lat1, @PathVariable BigDecimal lng1){
        Location l=locationService.closest(new Location(lat1,lng1));
        return pathPointsRepository.getIDByLatLng(l.getLocationLat(),l.getLocationLng());
    }
    @GetMapping(path="/checkoutNearestStationsTest/{id}")
    public List<NearestStation> checkoutNearestStations(@PathVariable Long id){
        return stationService.checkoutNearestStations(id);
    }

    @GetMapping(path = {"/is-active"})
    public boolean isActive(){
        Train train = new Train();
        train.setId(5L);
        train.setLocationLat(BigDecimal.valueOf(5));
        train.setLocationLng(BigDecimal.valueOf(5));
        train.setDirection("AA");
        train.setType("dsa");
        return locationService.isTrainActive(train);
    }
    @GetMapping(path = {"/has-passed"})
    public boolean hasPassed() {
        Location location1 = new Location(BigDecimal.valueOf(26.696219951117374), BigDecimal.valueOf(31.602375753873698));
        Location location2 = new Location(BigDecimal.valueOf(26.76833502505966), BigDecimal.valueOf(31.506010858006096));
        Location location3 = new Location(BigDecimal.valueOf(26.905410706549052), BigDecimal.valueOf(31.431620024564662));
        Train train = new Train();
        train.setId(5L);
        train.setLocationLat(location1.getLocationLat());
        train.setLocationLng(location1.getLocationLng());
        train.setDirection("UP");
        train.setType("dsa");
        return locationService.hasTrainPassedCity(train, location2, location3);
    }
    @GetMapping(path = {"/distance"})
    public double getDistance(){
        Location location1=new Location(BigDecimal.valueOf(26.551245),BigDecimal.valueOf(31.699417));
        Location location2 =new Location(BigDecimal.valueOf(26.551401),BigDecimal.valueOf(31.69921));
        return locationService.distance(location1, location2);
    }

    @GetMapping(path = {"/LinearClosest/{lat1}-{lng1}"})
    public int c2(@PathVariable BigDecimal lat1, @PathVariable BigDecimal lng1){
        Location l=locationService.closest(new Location(lat1,lng1));
        return pathPointsRepository.getIDByLatLng( l.getLocationLat(),l.getLocationLng());
    }

    @GetMapping(path = {"/nearest-stations"})
    public List<NearestStation> getNearestStation(@RequestParam("user-id")Long userId, @RequestParam("train-id") Long id) {
        // pointsHistoryService.points(userId,"Nearest Station");
        return stationService.checkoutNearestStations( id);
    }

    @GetMapping(path = {"/test-points"})
    public void addPoints(){
        AppUser appUser = appUserRepository.getById(1L);
        pointsHistoryService.points(1L,"Bad Share");
        appUserRepository.saveAndFlush(appUser);
    }
}


