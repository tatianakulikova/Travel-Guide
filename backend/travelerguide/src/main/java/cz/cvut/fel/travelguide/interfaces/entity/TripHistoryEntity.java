package cz.cvut.fel.travelguide.interfaces.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "TRIP_HISTORY")
public class TripHistoryEntity {

    @Id
    @Column(name = "TRIP_HISTORY_ID")
    @NotNull
    @GeneratedValue
    private Integer id;

    @Column(name = "ORIGIN_PLACE")
    @NotNull
    @Size(max = 100)
    private String originPlace;

    @Column(name = "DESTINATION_PLACE")
    @NotNull
    @Size(max = 100)
    private String destinationPlace;

    @Column(name = "TRIP_MODE")
    @NotNull
    @Size(max = 20)
    private String tripMode;

    @Column(name = "FEEDBACK")
    @Size(max = 1000)
    private String feedback;

    @Column(name = "DISTANCE")
    @Size(max = 1000)
    private String distance;

    @Column(name = "DURATION")
    @Size(max = 1000)
    private String duration;

    @Column(name = "LINK")
    @Size(max = 1000)
    private String link;

    @Column(name = "STATIC_MAP")
    @Size(max = 10000)
    private String staticMap;

    @Column(name = "CREATED_AT")
    @NotNull
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "EMAIL")
    private UserInfoEntity user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(String destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public String getTripMode() {
        return tripMode;
    }

    public void setTripMode(String tripMode) {
        this.tripMode = tripMode;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStaticMap() {
        return staticMap;
    }

    public void setStaticMap(String staticMap) {
        this.staticMap = staticMap;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserInfoEntity getUser() {
        return user;
    }

    public void setUser(UserInfoEntity user) {
        this.user = user;
    }

}

