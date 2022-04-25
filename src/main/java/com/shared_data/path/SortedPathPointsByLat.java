package com.shared_data.path;

import com.location.Location;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SortedPathPointsByLat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLat = Location.DEFAULT_LOCATION;
    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLng = Location.DEFAULT_LOCATION;

}
