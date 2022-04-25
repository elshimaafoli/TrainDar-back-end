package com.station;

import com.train.Train;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class StationService {
    private final StationRepository stationRepository;
    public List<Station> findAll() {
        return stationRepository.findAll();
    }

}
