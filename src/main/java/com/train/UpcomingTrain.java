package com.train;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UpcomingTrain {
    private Long trainId;
    private String timeLeft;

    public UpcomingTrain(Long trainId, int timeLeft) {
        this.trainId = trainId;
        int hours, minutes, seconds;
        hours = timeLeft / 3600;
        timeLeft %= 3600;
        minutes = timeLeft / 60;
        seconds = timeLeft % 60;
        this.timeLeft = String.valueOf(hours) + " H: " + String.valueOf(minutes) + " M: " + String.valueOf(seconds) + " s";
    }
}
