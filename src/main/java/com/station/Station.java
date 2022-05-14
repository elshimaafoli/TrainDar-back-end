package com.station;

import com.location.Location;
import com.train_station.TrainStation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "station")
    List<TrainStation> trains;
    @Column(columnDefinition = "TEXT")
    private String name;
    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLat = Location.DEFAULT_LOCATION;
    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLng = Location.DEFAULT_LOCATION;


}
