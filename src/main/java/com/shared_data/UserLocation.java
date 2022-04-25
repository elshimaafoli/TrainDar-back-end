package com.shared_data;

import com.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class UserLocation {
    private long userId;
    private Location location;
}
