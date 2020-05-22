package cz.cvut.fel.travelguide.interfaces.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PLACE")
public class PlaceEntity {

    @Id
    @Column(name = "PLACE_ID")
    @NotNull
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "TRIP_HISTORY_ID")
    private TripHistoryEntity tripHistory;

    @Column(name = "NAME")
    @NotNull
    private String name;

    @Column(name = "WEBS")
    @NotNull
    private String webs;

    @Column(name = "PHONES")
    @NotNull
    private String phones;

    @Column(name = "HOURS")
    @NotNull
    private String hours;

    @Column(name = "ADDRESS")
    @NotNull
    private String address;

    @Column(name = "LINK")
    @NotNull
    private String link;

    @Column(name = "MAIN_PLACE")
    @NotNull
    private Boolean mainPlace;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TripHistoryEntity getTripHistory() {
        return tripHistory;
    }

    public void setTripHistory(TripHistoryEntity tripHistory) {
        this.tripHistory = tripHistory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebs() {
        return webs;
    }

    public void setWebs(String webs) {
        this.webs = webs;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getMainPlace() {
        return mainPlace;
    }

    public void setMainPlace(Boolean mainPlace) {
        this.mainPlace = mainPlace;
    }

}
