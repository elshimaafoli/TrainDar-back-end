package com.station;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class NearestStation {
    private String name;
    private String timeLeft;
    public NearestStation(String name, int timeLeft) {
        this.name = name;
        int hours, minutes, seconds;
        hours = timeLeft / 3600;
        timeLeft %= 3600;
        minutes = timeLeft / 60;
        seconds = timeLeft % 60;
        this.timeLeft = String.valueOf(hours) + " H: " + String.valueOf(minutes) + " M: " + String.valueOf(seconds) + " s";
    }
}
