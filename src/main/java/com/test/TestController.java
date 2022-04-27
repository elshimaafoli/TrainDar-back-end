package com.test;

import com.location.Location;
import com.location.LocationService;
import com.shared_data.path.PathPoints;
import com.shared_data.path.PathPointsRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/test")
public class TestController {
    LocationService locationService;
    List<Boolean> returnResults = new ArrayList<>();
    @GetMapping
    public List<Boolean> runTest(){
        returnResults.clear();
        returnResults.add(locationService.isPassThrough(3, 1));
        returnResults.add(locationService.isPassThrough(3, 2));
        returnResults.add(locationService.isPassThrough(3, 5));
        return returnResults;
    }
    @GetMapping(path = {"/aa"})
    public void test1(){
        System.out.println();
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

    public PathPointsRepository pathPointsRepository;
    @GetMapping(path = {"/pathPointsTest"})
    public List<PathPoints> pathPointsTest(){
        return pathPointsRepository.getAllPointsByLat();
    }

        @GetMapping(path = {"/pathPointsIDTest"})
    public Long pathPointsIDTest(){
        //26.551245,31.699417)
        return pathPointsRepository.getIDByLatLng(BigDecimal.valueOf(26.551245),BigDecimal.valueOf(31.699417));
    }

   /* @GetMapping(path = {"/timeLeftTest"})
    public double timeLeftTest(){
        Location location1=new Location(BigDecimal.valueOf(27.181265),BigDecimal.valueOf(31.187582)),
                location2=new Location(BigDecimal.valueOf(27.18112),BigDecimal.valueOf(31.187607));
        return locationService.timeLeft(location1,location2);
    }*/
}


