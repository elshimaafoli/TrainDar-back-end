package com.points;

import com.appuser.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class PointsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String
            fromName
            , toName
            , details;
    private LocalDateTime date;
    private Long amount;
    public PointsHistory(PointsHistory pointsHistory) {
        this.id = pointsHistory.id;
        this.fromName = pointsHistory.fromName;
        this.toName = pointsHistory.toName;
        this.amount = pointsHistory.amount;
        this.date = pointsHistory.date;
        this.details = pointsHistory.details;
    }
    @ManyToOne
    @JoinColumn(nullable = false, name = "app_user_id")
    @JsonIgnore
    private AppUser appUser;
}
