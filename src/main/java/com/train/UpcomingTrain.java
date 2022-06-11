package com.train;

import lombok.*;

import java.util.Collections;
import java.util.Comparator;

@Setter
@Getter
@EqualsAndHashCode
@ToString
//implements Comparable<User>
public class UpcomingTrain implements Comparable<UpcomingTrain>{
    private Long trainId;
    private int int_time_left;
    private String timeLeft;

    public UpcomingTrain(Long trainId, int timeLeft) {
        this.int_time_left=timeLeft;
        this.trainId = trainId;
        int hours, minutes, seconds;
        hours = timeLeft / 3600;
        timeLeft %= 3600;
        minutes = timeLeft / 60;
        seconds = timeLeft % 60;
        this.timeLeft = String.valueOf(hours) + " H: " + String.valueOf(minutes) + " M: " + String.valueOf(seconds) + " s";
    }
    @Override
    public int compareTo(UpcomingTrain t) {
        if (this.getInt_time_left()==t.getInt_time_left())
            return 0;
        else if (this.getInt_time_left()>t.getInt_time_left())
            return 1;
        else
            return -1;
        //return this.getInt_time_left().compareTo(t.getInt_time_left());
    }

}
