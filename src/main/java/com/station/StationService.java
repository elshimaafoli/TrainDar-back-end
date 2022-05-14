package com.station;

import com.location.Location;
import com.location.LocationRepository;
import com.location.LocationService;
import com.shared_data.path.PathPointsRepository;
import com.train.Train;
import com.train.TrainRepository;
import com.train_station.TrainStation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class StationService {
    private final StationRepository stationRepository;
    private final TrainRepository trainRepository;
    private final LocationService locationService;
    private final PathPointsRepository pathPointsRepository;

    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    public List<NearestStation>checkoutNearestStations(Long trainId){
        Train train=trainRepository.getById(trainId);
        List<NearestStation>nearestStations=new ArrayList<>();
        List<TrainStation>trainStations=train.getStations();

        for (var station : trainStations){

            Location trainLocation=new Location(train.getLocationLat(),train.getLocationLng()),
            stationLocation = new Location(station.getStation().getLocationLat(), station.getStation().getLocationLng());

            if (train.getDirection().equals(locationService.DirectionUpDown(trainLocation,stationLocation)))
            {
                nearestStations.add(
                        new NearestStation(station.getStation().getName(),
                                locationService.timeLeft(trainLocation,stationLocation)));
            }
        }
        return nearestStations;
    }


}
