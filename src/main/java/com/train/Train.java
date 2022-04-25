package com.train;

import com.appuser.AppUser;
import com.location.Location;
import com.train_station.TrainStation;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Train {
    //train's average speed m/s
    public static final int AVERAGE_SPEED = 25;
    @Id
    private Long id;
    @OneToMany(mappedBy = "train")
    private List<TrainStation> stations;

    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLat = Location.DEFAULT_LOCATION;
    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLng = Location.DEFAULT_LOCATION;
    @Column(nullable = false)
    private String direction;
    @Column(nullable = false)
    private String type;
    private LocalDateTime lastKnownTime;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "train", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<AppUser> sharedUsers;

}