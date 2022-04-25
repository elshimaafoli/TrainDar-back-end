package com.test;

import com.location.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
