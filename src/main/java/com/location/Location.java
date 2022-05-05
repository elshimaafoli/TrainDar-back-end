package com.location;

import lombok.*;

import java.math.BigDecimal;
@Data
public class Location {
    public static final BigDecimal DEFAULT_LOCATION = BigDecimal.valueOf(-1);
    private BigDecimal locationLat, locationLng;
    public static final double CLOSE = 30;

    public Location() {
        locationLat = BigDecimal.valueOf(0);
        locationLng = BigDecimal.valueOf(0);
    }

    public Location(BigDecimal locationLat, BigDecimal locationLng) {
        this.locationLat = locationLat;
        this.locationLng = locationLng;
    }
}