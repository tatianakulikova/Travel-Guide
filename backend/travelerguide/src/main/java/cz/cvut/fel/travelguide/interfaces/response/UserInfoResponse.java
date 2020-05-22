package cz.cvut.fel.travelguide.interfaces.response;

public class UserInfoResponse {

    private String email;
    private String destination;
    private Integer feedbackId;
    private Boolean useHistory;
    private Boolean feedback;
    private String status;

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

    public Boolean getFeedback() {
        return feedback;
    }

    public void setFeedback(Boolean feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

}
