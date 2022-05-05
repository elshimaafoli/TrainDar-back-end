package com.shared_data.path;

import com.location.Location;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PathPoints {
    @Id
    private Long id;
    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLat = Location.DEFAULT_LOCATION;
    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLng = Location.DEFAULT_LOCATION;
    public PathPoints(Long id,BigDecimal locationLat,BigDecimal locationLng){
        this.id=id;
        this.locationLat=locationLat;
        this.locationLng=locationLng;
    }
}
