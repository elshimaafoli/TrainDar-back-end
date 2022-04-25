package com.train_station;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class TrainStationKey implements Serializable {
    @Column(name = "train_id")
    private Long trainId;
    @Column(name = "station_id")
    private Long stationId;
}
