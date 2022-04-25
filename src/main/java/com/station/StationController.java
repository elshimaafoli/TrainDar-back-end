package com.station;

import com.train.Train;
import com.train.TrainRepository;
import com.train.TrainService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/station")
@CrossOrigin(origins = "*")
public class StationController {
    private final StationService stationService;

    @GetMapping(path = {"/all"})
    public List<Station> getStations() {
        return stationService.findAll();
    }
}