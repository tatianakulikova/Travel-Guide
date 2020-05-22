package cz.cvut.fel.travelguide.interfaces.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USER_INFO")
public class UserInfoEntity {

    @Id
    @Column(name = "EMAIL")
    @NotNull
    @Size(max = 50)
    private String email;

    @Column(name = "FEEDBACK")
    @NotNull
    private Boolean feedback;

    @Column(name = "FEEDBACK_ITERATION")
    @NotNull
    private Integer feedbackIteration;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getFeedback() {
        return feedback;
    }

    public void setFeedback(Boolean feedback) {
        this.feedback = feedback;
    }

    public Integer getFeedbackIteration() {
        return feedbackIteration;
    }

    public void setFeedbackIteration(Integer feedbackIteration) {
        this.feedbackIteration = feedbackIteration;
    }

    @PrePersist
    void preInsert() {
        if (this.feedback == null){
            this.feedback = true;
        }
        if (this.feedbackIteration == null) {
            this.feedbackIteration = 5;
        }
    }

}
