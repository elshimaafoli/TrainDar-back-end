package com.station;

import com.points.PointsHistoryService;
import com.train.Train;
import com.train.TrainRepository;
import com.train.TrainService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/station")
@CrossOrigin(origins = "*")
public class StationController {
    private final StationService stationService;
    private final PointsHistoryService pointsHistoryService;
    @GetMapping(path = {"/all"})
    public List<String> getStations() {
        return stationService.findAllCityNames();
    }

    @GetMapping(path = {"/nearest-stations"})
    public List<NearestStation> getNearestStation(@RequestParam("user-id")Long userId,@RequestParam("train-id") Long id) {
       pointsHistoryService.points(userId,"Nearest Station");
        return stationService.checkoutNearestStations( id);
    }

}