package com.train;

import com.appuser.AppUser;
import com.location.Location;
import com.location.LocationService;
import com.shared_data.UserLocation;
import com.shared_data.custom_data_types.Pair;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
@AllArgsConstructor
@Data
@Component
public class LocationFilteration {
    private final LocationService locationService;
    private static BigDecimal getAverage(List<Pair<BigDecimal, Long>> values){
        if(values.isEmpty()){
            return Location.DEFAULT_LOCATION;
        }
        BigDecimal sum = new BigDecimal(0);
        for(var value : values){
            sum = sum.add(value.first);
        }
        return sum.divide(BigDecimal.valueOf(values.size()),16, RoundingMode.HALF_UP);
    }
    public Location removeOutliers(List<AppUser> users) {
        //filter users to git only the users that on the railway line

        List<UserLocation> locations = new ArrayList<>();
        //TODO: remove the outliers
        TreeSet<Long> outLiers = new TreeSet<>();
        //git out users that are far away from railway line
        var tempOutLiers = locationService.outOfRange(users);
        for(var user : tempOutLiers){
            outLiers.add(user.getId());
        }
        var inRangeUsers = locationService.inRange(users);
        //convert from list of users to list of userLocation
        for(var user : inRangeUsers){
            locations.add(new UserLocation(user.getId(), new Location(user.getLocationLat(), user.getLocationLng())));
        }
        if(locations.isEmpty()){
            return new Location(Location.DEFAULT_LOCATION,Location.DEFAULT_LOCATION);
        }
        if(locations.size() == 1){
            return locations.get(0).getLocation();
        }
        List<Pair<BigDecimal, Long>> lats = new ArrayList<>(),
                lngs = new ArrayList<>(),
                filteredLats = new ArrayList<>(),
                filteredLngs = new ArrayList<>();
        BigDecimal latQ1, latQ3, latIQR, lngQ1, lngQ3, lngIQR;
        for (var userLocation : locations) {
            lngs.add(new Pair<>(userLocation.getLocation().getLocationLng(), userLocation.getUserId()));
            lats.add(new Pair<>(userLocation.getLocation().getLocationLat(), userLocation.getUserId()));
        }
        Collections.sort(lngs);
        Collections.sort(lats);
        int sz = lats.size();
        if (sz % 2 == 0) {//if vector size is even, mid is two values
            if (sz % 4 == 0) {//then q1,q3 have 2 values each
                //calculate q1,q3 for lat
                latQ1 = lats.get(sz / 4).first.add(lats.get(sz / 4 - 1).first).divide(BigDecimal.valueOf(2.0),16, RoundingMode.HALF_UP);
                latQ3 = lats.get(sz * 3 / 4).first.add(lats.get(sz * 3 / 4 - 1).first).divide(BigDecimal.valueOf(2.0),16, RoundingMode.HALF_UP);
                //calculate q1,q3 for lon
                lngQ1 = lngs.get(sz / 4).first.add(lngs.get(sz / 4 - 1).first).divide(BigDecimal.valueOf(2.0),16, RoundingMode.HALF_UP);
                lngQ3 = lngs.get(sz * 3 / 4).first.add(lngs.get(sz * 3 / 4 - 1).first).divide(BigDecimal.valueOf(2.0),16, RoundingMode.HALF_UP);
            } else {//then q1,q3 have 1 value each
                //calculate q1,q3 for lat
                latQ1 = lats.get(sz / 4).first;
                latQ3 = lats.get(sz * 3 / 4).first;
                //calculate q1,q3 for lon
                lngQ1 = lngs.get(sz / 4).first;
                lngQ3 = lngs.get(sz * 3 / 4).first;
            }
        } else {//if vector size is odd, mid is one value
            if ((sz - 1) % 4 == 0) {//then q1,q3 have 2 values each
                //calculate q1,q3 for lat
                latQ1 = lats.get(sz / 4).first.add(lats.get(sz / 4 - 1).first).divide(BigDecimal.valueOf(2.0),16, RoundingMode.HALF_UP);
                latQ3 = lats.get(sz * 3 / 4).first.add(lats.get(sz * 3 / 4 + 1).first).divide(BigDecimal.valueOf(2.0),16, RoundingMode.HALF_UP);
                //calculate q1,q3 for lon
                lngQ1 = lngs.get(sz / 4).first.add(lngs.get(sz / 4 - 1).first).divide(BigDecimal.valueOf(2.0),16, RoundingMode.HALF_UP);
                lngQ3 = lngs.get(sz * 3 / 4).first.add(lngs.get(sz * 3 / 4 + 1).first).divide(BigDecimal.valueOf(2.0),16, RoundingMode.HALF_UP);
            } else {//then q1,q3 have 1 value each
                //calculate q1,q3 for lat
                latQ1 = lats.get(sz / 4).first;
                latQ3 = lats.get(sz * 3 / 4).first;
                //calculate q1,q3 for lon
                lngQ1 = lngs.get(sz / 4).first;
                lngQ3 = lngs.get(sz * 3 / 4).first;
            }
        }
        latIQR = (latQ3.subtract(latQ1)).multiply(BigDecimal.valueOf(1.5));
        lngIQR = (lngQ3.subtract(lngQ1)).multiply(BigDecimal.valueOf(1.5));
        //loop for removing outlier
        //outliers is bigger than Q3+IQR or smaller than Q1-IQR
        for (int i = 0; i < sz; i++) {
            if (lats.get(i).first.compareTo(latQ3.add(latIQR)) > 0 || lats.get(i).first.compareTo(latQ1.subtract(latIQR)) < 0) {
                outLiers.add(lats.get(i).second);
            } else {
                filteredLats.add(new Pair<>(lats.get(i).first, lats.get(i).second));
            }
            if (lngs.get(i).first.compareTo(lngQ3.add(lngIQR)) > 0 || lngs.get(i).first.compareTo(lngQ1.subtract(lngIQR)) < 0) {
                outLiers.add(lngs.get(i).second);
            } else {
                filteredLngs.add(new Pair<>(lngs.get(i).first, lngs.get(i).second));
            }
        }
        return new Location(getAverage(filteredLats), getAverage(filteredLngs));
    }
}