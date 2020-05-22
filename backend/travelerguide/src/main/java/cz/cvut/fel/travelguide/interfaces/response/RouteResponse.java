package cz.cvut.fel.travelguide.interfaces.response;

public class RouteResponse {
    private String totalDistance;
    private String totalTime;
    private String fare;
    private String instructions;
    private String routePicture;
    private String routeLink;

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getRoutePicture() {
        return routePicture;
    }

    public void setRoutePicture(String routePicture) {
        this.routePicture = routePicture;
    }

    public String getRouteLink() {
        return routeLink;
    }

    public void setRouteLink(String routeLink) {
        this.routeLink = routeLink;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }
}
