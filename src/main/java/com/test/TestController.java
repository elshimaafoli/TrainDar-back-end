package com.test;

import com.location.Location;
import com.location.LocationService;
import com.shared_data.path.PathPoints;
import com.shared_data.path.PathPointsRepository;
import com.station.NearestStation;
import com.station.Station;
import com.station.StationRepository;
import com.station.StationService;
import com.train.TrainService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/test")
public class TestController {
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

    @GetMapping(path = {"/test2"})
    public boolean runTest2() {
        //27.198504550269035, 31.10727234294203
        Location l1=new Location(BigDecimal.valueOf(27.198504550269035),BigDecimal.valueOf(31.10727234294203));
        //27.201152005114828, 31.102807419258447
        Location l2=new Location(BigDecimal.valueOf(27.201152005114828),BigDecimal.valueOf(31.102807419258447));
        //27.206005509163948, 31.09474115795374
        Location l3=new Location(BigDecimal.valueOf(27.206005509163948),BigDecimal.valueOf(31.09474115795374));
        return locationService.hasTrainPassedCity(l1,l3,l2);
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
        return pathPointsRepository.getIDByLatLng(BigDecimal.valueOf(27.181265), BigDecimal.valueOf(31.187582));
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
    //26.55654923372615-31.693084504454426-26.557952111875835-31.69158138626245 //UP

    @GetMapping(path = {"/LinearrClosest/{lat1}-{lng1}"})
    public int c2(@PathVariable BigDecimal lat1, @PathVariable BigDecimal lng1){
        Location l=locationService.linearClosest(new Location(lat1,lng1));
        return pathPointsRepository.getIDByLatLng( l.getLocationLat(),l.getLocationLng());
    }
    //26.55654923372615-31.693084504454426-26.557952111875835-31.69158138626245 //UP

    @GetMapping(path="/checkoutNearestStationsTest/{id}")
    public List<NearestStation> checkoutNearestStations(@PathVariable Long id){
        return stationService.checkoutNearestStations(id);
    }

}


