package cz.cvut.fel.travelguide.interfaces.request;

import cz.cvut.fel.travelguide.interfaces.enums.RequestEnum;
import cz.cvut.fel.travelguide.interfaces.enums.TripTypeEnum;

import javax.validation.constraints.NotNull;

public class BaseRequest {

    @NotNull
    private RequestEnum requestType;
    private String placeName;
    private String origin;
    private String coordinates;
    private String destination;
    private TripTypeEnum tripType;
    private String email;
    private Boolean useHistory;
    private Integer feedbackId;
    private String feedback;
    private String category;
    private Boolean feedbackStop;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public RequestEnum getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestEnum requestType) {
        this.requestType = requestType;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public TripTypeEnum getTripType() {
        return tripType;
    }

    public void setTripType(TripTypeEnum tripType) {
        this.tripType = tripType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getUseHistory() {
        return useHistory;
    }

    public void setUseHistory(Boolean useHistory) {
        this.useHistory = useHistory;
    }

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getFeedbackStop() {
        return feedbackStop;
    }

    public void setFeedbackStop(Boolean feedbackStop) {
        this.feedbackStop = feedbackStop;
    }

}
