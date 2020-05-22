package cz.cvut.fel.travelguide.interfaces.response;

public class BaseResponse {

    private String response;

    public BaseResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
