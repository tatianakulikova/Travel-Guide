package cz.cvut.fel.travelguide.interfaces.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EMAIL_TEMPLATE")
public class EmailTemplateEntity {

    @Id
    @Column(name = "EMAIL_TEMPLATE_ID")
    @NotNull
    @GeneratedValue
    private Integer id;

    @Column(name = "TEMPLATE")
    @NotNull
    private String template;

    @Column(name = "PLACE_NAME_TEMPLATE")
    @NotNull
    private String placeNameTemplate;

    @Column(name = "PLACE_HOURS_TEMPLATE")
    @NotNull
    private String placeHoursTemplate;

    @Column(name = "PLACE_PHONE_TEMPLATE")
    @NotNull
    private String placePhoneTemplate;

    @Column(name = "PLACE_WEB_TEMPLATE")
    @NotNull
    private String placeWebTemplate;

    @Column(name = "PLACE_ADDRESS_TEMPLATE")
    @NotNull
    private String placeAddressTemplate;

    @Column(name = "KM_TEMPLATE")
    @NotNull
    private String kmTemplate;

    @Column(name = "TIME_TEMPLATE")
    @NotNull
    private String timeTemplate;

    @Column(name = "STATIC_MAP_TEMPLATE")
    @NotNull
    private String staticMapTemplate;

    @Column(name = "LINK_TEMPLATE")
    @NotNull
    private String linkTemplate;

    @Column(name = "PLACES_NAME_TEMPLATE")
    @NotNull
    private String placesNameTemplate;

    @Column(name = "PLACES_PHONE_TEMPLATE")
    @NotNull
    private String placesPhoneTemplate;

    @Column(name = "PLACES_WEB_TEMPLATE")
    @NotNull
    private String placesWebTemplate;

    @Column(name = "PLACES_HOURS_TEMPLATE")
    @NotNull
    private String placesHoursTemplate;

    @Column(name = "PLACES_TEMPLATE")
    @NotNull
    private String placesTemplate;

    @Column(name = "PLACES_LINK_TEMPLATE")
    @NotNull
    private String placesLinkTemplate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getPlaceNameTemplate() {
        return placeNameTemplate;
    }

    public void setPlaceNameTemplate(String placeNameTemplate) {
        this.placeNameTemplate = placeNameTemplate;
    }

    public String getPlaceHoursTemplate() {
        return placeHoursTemplate;
    }

    public void setPlaceHoursTemplate(String placeHoursTemplate) {
        this.placeHoursTemplate = placeHoursTemplate;
    }

    public String getPlacePhoneTemplate() {
        return placePhoneTemplate;
    }

    public void setPlacePhoneTemplate(String placePhoneTemplate) {
        this.placePhoneTemplate = placePhoneTemplate;
    }

    public String getPlaceWebTemplate() {
        return placeWebTemplate;
    }

    public void setPlaceWebTemplate(String placeWebTemplate) {
        this.placeWebTemplate = placeWebTemplate;
    }

    public String getPlaceAddressTemplate() {
        return placeAddressTemplate;
    }

    public void setPlaceAddressTemplate(String placeAddressTemplate) {
        this.placeAddressTemplate = placeAddressTemplate;
    }

    public String getKmTemplate() {
        return kmTemplate;
    }

    public void setKmTemplate(String kmTemplate) {
        this.kmTemplate = kmTemplate;
    }

    public String getTimeTemplate() {
        return timeTemplate;
    }

    public void setTimeTemplate(String timeTemplate) {
        this.timeTemplate = timeTemplate;
    }

    public String getStaticMapTemplate() {
        return staticMapTemplate;
    }

    public void setStaticMapTemplate(String staticMapTemplate) {
        this.staticMapTemplate = staticMapTemplate;
    }

    public String getLinkTemplate() {
        return linkTemplate;
    }

    public void setLinkTemplate(String linkTemplate) {
        this.linkTemplate = linkTemplate;
    }

    public String getPlacesNameTemplate() {
        return placesNameTemplate;
    }

    public void setPlacesNameTemplate(String placesNameTemplate) {
        this.placesNameTemplate = placesNameTemplate;
    }

    public String getPlacesPhoneTemplate() {
        return placesPhoneTemplate;
    }

    public void setPlacesPhoneTemplate(String placesPhoneTemplate) {
        this.placesPhoneTemplate = placesPhoneTemplate;
    }

    public String getPlacesWebTemplate() {
        return placesWebTemplate;
    }

    public void setPlacesWebTemplate(String placesWebTemplate) {
        this.placesWebTemplate = placesWebTemplate;
    }

    public String getPlacesHoursTemplate() {
        return placesHoursTemplate;
    }

    public void setPlacesHoursTemplate(String placesHoursTemplate) {
        this.placesHoursTemplate = placesHoursTemplate;
    }

    public String getPlacesTemplate() {
        return placesTemplate;
    }

    public void setPlacesTemplate(String placesTemplate) {
        this.placesTemplate = placesTemplate;
    }

    public String getPlacesLinkTemplate() {
        return placesLinkTemplate;
    }

    public void setPlacesLinkTemplate(String placesLinkTemplate) {
        this.placesLinkTemplate = placesLinkTemplate;
    }

}
