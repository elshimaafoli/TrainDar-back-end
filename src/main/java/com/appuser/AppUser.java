package com.appuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.location.Location;
import com.points.PointsHistory;
import com.train.Train;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long points=0L;
    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    //**************TODO:password must be less than 10****************;
    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false, length = 15)
    private String phone;
    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLat = Location.DEFAULT_LOCATION;
    @Column(columnDefinition = "TEXT")
    private BigDecimal locationLng = Location.DEFAULT_LOCATION;

    @ManyToOne
    @JoinColumn(name="train_id")
    @JsonIgnore
    private Train train;
    /*
    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate birthDate;
     */

    private Boolean locked = false;

    private Boolean enabled = true;
    /*
    @Column(length = 64)
    private String uniqueCode;
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] userPhoto;
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "appUser")
    private List<PointsHistory> pointsHistory;
    public AppUser(String name, String email, String password, String phone/*, LocalDate birthDate*/) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        //this.birthDate = birthDate;
    }

    public AppUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
