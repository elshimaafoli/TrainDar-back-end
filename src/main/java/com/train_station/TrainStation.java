package com.train_station;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.station.Station;
import com.train.Train;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TrainStation {
    @EmbeddedId
    private TrainStationKey id;
    @ManyToOne
    @JsonIgnore
    @MapsId("trainId")
    @JoinColumn(name = "train_id")
    private Train train;
    @ManyToOne
    @JsonIgnore
    @MapsId("stationId")
    @JoinColumn(name = "station_id")
    private Station station;
    private LocalDateTime arrivalTime;
}