package com.location;

import com.appuser.AppUser;
import com.shared_data.path.PathPoints;
import com.shared_data.path.PathPointsRepository;
import com.station.Station;
import com.station.StationRepository;
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
    //TODO: function take list of users and return the out of range users ,
    //TODO: direction function
    //DONE: sort the path points by longitude will be sorted before insertion
    //DONE: sort the path points by latitude will be sorted before insertion
    //DONE: closest for longitude and compare the closest longitude and the latitude then return the closest locatin
    //DONE
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
        PathPointsRepository pathPointsRepository = null;
        //suppose it is sorted
        pathPoints = (ArrayList<PathPoints>) pathPointsRepository.getAllPointsByLat();
        //binary search for closest point
        int first = 0;
        int last = pathPoints.size() - 1;
        int middle = (first + last) / 2;
        double closeValue = Location.CLOSE;
        Location middleLocation;
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
        return new Location(BigDecimal.valueOf(0), BigDecimal.valueOf(0));
    }

    public Location closestLng(Location shared) {//sorted by lngs

        ArrayList<PathPoints> pathPoints;
        PathPointsRepository pathPointsRepository = null;
        //suppose it is sorted
        pathPoints = (ArrayList<PathPoints>) pathPointsRepository.getAllPointsByLng();
        //binary search for closest point
        int first = 0;
        int last = pathPoints.size() - 1;
        int middle = (first + last) / 2;
        double closeValue = Location.CLOSE;
        Location  middleLocation;

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

        return new Location(BigDecimal.valueOf(0), BigDecimal.valueOf(0));
    }

    public Location closest (Location shared){
        Location closestLat=closestLat(shared);
        Location closestLng=closestLng(shared);
        if (closestLng.getLocationLat().doubleValue()==0
                &&closestLng.getLocationLng().doubleValue()==0
                &&closestLat.getLocationLat().doubleValue()==0
                &&closestLat.getLocationLng().doubleValue()==0)
            return new Location(BigDecimal.valueOf(0),BigDecimal.valueOf(0));
        else {
            if (distance(shared,closestLat)<distance(shared,closestLng))
                return closestLat;
            else return closestLng;
        }
    }

    public String stationDirection(String s1, String s2) {

        //  note that stations stored from aswan to cairo
        StationRepository stationRepository = null;
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
        PathPointsRepository pathPointsRepository=null;
        from=closest(from);
        to=closest(to);
        if (pathPointsRepository.getIDByLatLng(from.getLocationLat(),from.getLocationLng())>
                pathPointsRepository.getIDByLatLng(to.getLocationLat(),to.getLocationLng())){
            return "DOWN";
        }
        else if (pathPointsRepository.getIDByLatLng(from.getLocationLat(),from.getLocationLng())>
                pathPointsRepository.getIDByLatLng(to.getLocationLat(),to.getLocationLng())){
            return "UP";
        }
        else return "SAME";
    }

    boolean out(Location location){
        if (location.getLocationLat().doubleValue()==0&&location.getLocationLng().doubleValue()==0)return true;
        return false;
    }

    public List<AppUser> outOfRange(List<AppUser> users) {
        List<AppUser>outs=null;
        List<AppUser>ins=null;
        for (AppUser user : users) {
            Location shared=new Location(user.getLocationLat(),user.getLocationLng());
            if (out(closest(shared))){
                outs.add(user);
            }
            else {
                ins.add(user);
            }
        }
        return outs;
    }
    public List<AppUser> inRange(List<AppUser> users) {
        List<AppUser>ins=null;
        Location shared;
        for (AppUser user : users) {
            shared=new Location(user.getLocationLat(),user.getLocationLng());
            if (!out(closest(shared))){
                ins.add(user);
            }
        }
        return ins;
    }

    //given train id & station id this function will return if this train pass Through this station or not
    public boolean isPassThrough(long trainId, long stationId){
        var existedTrain = trainRepository.findById(trainId);
        var existedStation = stationRepository.findById(stationId);
        if(existedTrain.isEmpty() || existedStation.isEmpty()){
            return false;
        }
        //iterate over all this train's stations and check if the station exists or not
        for(var station : existedTrain.get().getStations()){
            if(station.getStation().getId() == stationId){
                //station found
                return true;
            }
        }
        //station not found
        return false;
    }

}
