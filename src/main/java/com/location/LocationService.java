package com.location;

import com.appuser.AppUser;
import com.shared_data.path.PathPoints;
import com.shared_data.path.PathPointsRepository;
import com.station.Station;
import com.station.StationRepository;
import com.train.Train;
import com.train.TrainRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {
    private StationRepository stationRepository;
    private final TrainRepository trainRepository;
    private final PathPointsRepository pathPointsRepository;

    public double distance(Location point1, Location point2) {
        double locationDistance = 0;
        BigDecimal r2d = BigDecimal.valueOf((180.0D) / (3.141592653589793D));
        final BigDecimal d2r = BigDecimal.valueOf(3.141592653589793D / 180.0D);
        final BigDecimal d2km = r2d.multiply(BigDecimal.valueOf(111189.57696D));
        BigDecimal x = d2r.multiply(point1.getLocationLat());
        BigDecimal y = d2r.multiply(point2.getLocationLat());
        double a = Math.sin(x.doubleValue()),
                b = Math.sin(y.doubleValue()),
                c = Math.cos(x.doubleValue()),
                d = Math.cos(y.doubleValue()),
                e = d2r.doubleValue(),
                f = point1.getLocationLng().doubleValue(),
                g = point2.getLocationLng().doubleValue();
        locationDistance = Math.acos(a * b + c * d * Math.cos(e * (f - g))) * d2km.doubleValue();

        return locationDistance;
    }

    public Location closestLat(Location shared) { //sorted by lats
        ArrayList<PathPoints> pathPoints;
        //suppose it was sorted
        pathPoints = pathPointsRepository.getAllPointsByLat();
        //binary search for closest point
        int first = 0;
        int last = pathPoints.size() - 1;
        int middle = (first + last) / 2;
        double closeValue = Location.CLOSE;
        Location middleLocation = new Location();
        while (first <= last) {
            middleLocation = new Location(pathPoints.get(middle).getLocationLat(), pathPoints.get(middle).getLocationLng());
            if (Math.abs(distance(shared, middleLocation)) <= closeValue) {
                return middleLocation;
            } else if (shared.getLocationLat().doubleValue() > middleLocation.getLocationLat().doubleValue()) {
                first = middle + 1;
            } else if (shared.getLocationLat().doubleValue() < middleLocation.getLocationLat().doubleValue()) {
                last = middle - 1;
            }
            middle = (first + last) / 2;
        }
//        return new Location(BigDecimal.valueOf(0), BigDecimal.valueOf(0));
        return middleLocation;
    }

    public Location closestLng(Location shared) {//sorted by lngs

        ArrayList<PathPoints> pathPoints;
        //suppose it is sorted
        pathPoints = pathPointsRepository.getAllPointsByLng();
        //binary search for closest point
        int first = 0;
        int last = pathPoints.size() - 1;
        int middle = (first + last) / 2;
        double closeValue = Location.CLOSE;
        Location middleLocation = new Location();

        while (first <= last) {
            middleLocation = new Location(pathPoints.get(middle).getLocationLat(), pathPoints.get(middle).getLocationLng());
            if (Math.abs(distance(shared, middleLocation)) <= closeValue) {
                return middleLocation;
            } else if (shared.getLocationLng().doubleValue() > middleLocation.getLocationLng().doubleValue()) {
                first = middle + 1;
            } else if (shared.getLocationLng().doubleValue() < middleLocation.getLocationLng().doubleValue()) {
                last = middle - 1;
            }
            middle = (first + last) / 2;
        }
        return middleLocation;
        //return new Location(BigDecimal.valueOf(0), BigDecimal.valueOf(0));
    }

    public Location binaryClosest(Location shared) {
        Location closestLat = closestLat(shared);
        Location closestLng = closestLng(shared);
        if (closestLng.getLocationLat().doubleValue() == 0
                && closestLng.getLocationLng().doubleValue() == 0
                && closestLat.getLocationLat().doubleValue() == 0
                && closestLat.getLocationLng().doubleValue() == 0)
            return new Location(BigDecimal.valueOf(0), BigDecimal.valueOf(0));
        else {
            if (distance(shared, closestLat) < distance(shared, closestLng))
                return closestLat;
            else return closestLng;
        }
    }

    public Location closest(Location shared) {
        ArrayList<PathPoints> pathPoints;
        //suppose it is sorted
        pathPoints = pathPointsRepository.findAll();
        double distance = 1e18;
        Location closeLocation = new Location(), temp = new Location();
        for (int i = 0; i < pathPoints.size(); i++) {
            temp = new Location(pathPoints.get(i).getLocationLat(), pathPoints.get(i).getLocationLng());
            if (distance > distance(shared, temp)) {
                closeLocation = temp;
                distance = distance(shared, temp);
            }
        }
        return closeLocation;
    }


    public String stationDirection(String s1, String s2) {

        //  note that stations stored from aswan to cairo
        Station station1 = stationRepository.findByName(s1);
        Station station2 = stationRepository.findByName(s2);
        if (station1.getId() > station2.getId()) return "DOWN";
            //down ...the train run from South to North
        else if (station1.getId() < station2.getId()) return "UP";
            //up... the train run from North to South
        else return "SAME";
        //if (station1.getId()<station2.getId())
    }

    public String DirectionUpDown(Location from, Location to) {
        //path points inserted from aswan to cairo
        from = closest(from);
        to = closest(to);
        int fromIndex = pathPointsRepository.getIDByLatLng(from.getLocationLat(), from.getLocationLng()), toIndex = pathPointsRepository.getIDByLatLng(to.getLocationLat(), to.getLocationLng());
        if (fromIndex > toIndex) {
            return "DOWN";
        } else if (fromIndex < toIndex) {
            return "UP";
        } else return "SAME";
    }

    boolean out(Location location) {
        Location close = closest(location);
        if (distance(close, location) > 30d)
            return true;
        return false;
    }

    public List<AppUser> outOfRange(List<AppUser> users) {
        List<AppUser> outs = new ArrayList<>();
        for (AppUser user : users) {
            Location shared = new Location(user.getLocationLat(), user.getLocationLng());
            if (out(closest(shared))) {
                outs.add(user);
            }
        }
        return outs;
    }

    public List<AppUser> inRange(List<AppUser> users) {
        List<AppUser> ins = new ArrayList<>();
        Location shared;
        for (AppUser user : users) {
            shared = new Location(user.getLocationLat(), user.getLocationLng());
            if (!out(closest(shared))) {
                ins.add(user);
            }
        }
        return ins;
    }

    //given train id & station id this function will return if this train pass Through this station or not
    public boolean isPassThrough(long trainId, long stationId) {
        var existedTrain = trainRepository.findById(trainId);
        var existedStation = stationRepository.findById(stationId);
        if (existedTrain.isEmpty() || existedStation.isEmpty()) {
            return false;
        }
        //iterate over all this train's stations and check if the station exists or not
        for (var station : existedTrain.get().getStations()) {
            if (station.getStation().getId() == stationId) {
                //station found
                return true;
            }
        }
        //station not found
        return false;
    }

    public boolean isTrainActive(Train train) {
        if (train.getLocationLat().equals(Location.DEFAULT_LOCATION) || train.getLocationLng().equals(Location.DEFAULT_LOCATION))
            return false;
        return true;
    }

    public boolean hasTrainPassedCity(Train train, Location firstCity, Location secondCity) {
        Location trainLocation = new Location(train.getLocationLat(), train.getLocationLng());
        if (DirectionUpDown(firstCity, secondCity).equals(train.getDirection()) && DirectionUpDown(firstCity, secondCity).equals(DirectionUpDown(trainLocation, firstCity)))
            return false;
        return true;
    }

    public int timeLeft(Location location1, Location location2) {
        /*returns the time left to reach the destination , it returns seconds */
        return (int) ((Math.abs(distance(location1, location2)) / Train.AVERAGE_SPEED));
    }

}
