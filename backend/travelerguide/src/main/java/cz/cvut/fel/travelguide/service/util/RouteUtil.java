package cz.cvut.fel.travelguide.service.util;

import cz.cvut.fel.travelguide.interfaces.api.RouteApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RouteUtil {

    private static String routeStaticMapUrl;
    private static String routeMapLink;
    private static String routeApiKey;

    @Value("${route.static.map.url}")
    public void setRouteStaticMapUrl(String staticMapUrl){
        routeStaticMapUrl = staticMapUrl;
    }

    @Value("${route.map.link.direction}")
    public void setRouteMapLink(String mapLink){
        routeMapLink = mapLink;
    }

    @Value("${route.api.key}")
    public void setRouteApiKey(String apiKey){
        routeApiKey = apiKey;
    }

    public static void getHtmlInstructions(RouteApiResponse.Route.Leg.Step step){
        if (step.getHtmlInstructionsString() != null && !"".equals(step.getHtmlInstructionsString())
                    && step.getDistance() != null && step.getDistance().getText() != null
                    && !"".equals(step.getDistance().getText())){
            step.setHtmlInstructions(step.getHtmlInstructionsString()
                    .concat(" - " + step.getDistance().getText()));
            if (step.getDuration() != null && step.getDuration().getText() != null
                    && !"".equals(step.getDuration().getText())){
                step.setHtmlInstructions(step.getHtmlInstructionsString()
                        .concat(" (" + step.getDuration().getText().replace(",", "") + ")"));
            }
            if (step.getTransitDetails() != null && step.getTransitDetails() != null
                    && step.getTransitDetails().getLine() != null
                    && step.getTransitDetails().getLine().getShortName() != null){
                step.setHtmlInstructions("Linka " + step.getTransitDetails().getLine().getShortName()
                        + " - " + step.getHtmlInstructionsString());
            }
            if (step.getTransitDetails() != null && step.getTransitDetails() != null
                    && step.getTransitDetails().getNumStops() != null){
                String numberStops = step.getTransitDetails().getNumStops() + " zast.)";
                if (step.getHtmlInstructionsString().contains(")")){
                    step.setHtmlInstructions(step.getHtmlInstructionsString()
                            .substring(0, step.getHtmlInstructionsString().length() - 1) + ", " + numberStops);
                } else {
                    step.setHtmlInstructions(step.getHtmlInstructionsString() + " (" + numberStops);
                }
            }
            step.setCreated(true);
        }
    }

    public static void getTotalDistance(RouteApiResponse response){
        if (response.getRoutes() != null && !response.getRoutes().isEmpty()
                && response.getRoutes().get(0).getLegs() != null
                && !response.getRoutes().get(0).getLegs().isEmpty()
                && response.getRoutes().get(0).getLegs().get(0).getDistance() != null){
            response.setTotalDistance(response.getRoutes().get(0).getLegs().get(0).getDistance().getText());
        }
    }

    public static void getTotalTime(RouteApiResponse response){
        if (response.getRoutes() != null && !response.getRoutes().isEmpty()
                && response.getRoutes().get(0).getLegs() != null
                && !response.getRoutes().get(0).getLegs().isEmpty()
                && response.getRoutes().get(0).getLegs().get(0).getDuration() != null){
            response.setTotalTime(response.getRoutes().get(0).getLegs().get(0).getDuration().getText());
        }
    }

    public static void getInstructions(RouteApiResponse response){
        if (response.getRoutes() != null && !response.getRoutes().isEmpty()
                && response.getRoutes().get(0).getLegs() != null
                && !response.getRoutes().get(0).getLegs().isEmpty()
                && response.getRoutes().get(0).getLegs().get(0).getSteps() != null) {
            final String[] instructions = {""};
            if (response.getRoutes().get(0).getLegs().get(0).getSteps().size() > 10) {
                response.getRoutes().get(0).getLegs().get(0).getSteps().stream()
                        .filter(step -> step.getHtmlInstructions() != null && step.getDistance() != null
                                && step.getDistance().getValue() != null && 999 < step.getDistance().getValue())
                        .forEach(step -> instructions[0] = instructions[0]
                                .concat(step.getHtmlInstructions()).concat(System.lineSeparator()));
            } else {
                response.getRoutes().get(0).getLegs().get(0).getSteps().stream()
                        .filter(step -> step.getHtmlInstructions() != null)
                        .forEach(step -> instructions[0] = instructions[0]
                                .concat(step.getHtmlInstructions()).concat(System.lineSeparator()));
            }
            response.setInstructions(instructions[0].substring(0, instructions[0].length() - 1));
        }
    }

    public static void getRoutePicture(RouteApiResponse response){
        if (response.getRoutes() != null && !response.getRoutes().isEmpty()
                && response.getRoutes().get(0).getOverviewPolyline() != null) {
            response.setRoutePicture( routeStaticMapUrl + "?size=500x400" +
                    "&language=cs&key=" + routeApiKey + "&path=enc%3A"
                    + response.getRoutes().get(0).getOverviewPolyline().getPoints());
        }
    }

    public static void getRouteLink(RouteApiResponse response){
        if (response.getGeoCodedWaypoints() != null && response.getGeoCodedWaypoints().size() >= 2
                && response.getRoutes() != null && !response.getRoutes().isEmpty()
                && response.getRoutes().get(0).getLegs() != null && !response.getRoutes().get(0).getLegs().isEmpty()
                && response.getRoutes().get(0).getLegs().get(0).getStartAddress() != null
                && response.getRoutes().get(0).getLegs().get(0).getEndAddress() != null) {
            response.setRouteLink("<a href=\"" + routeMapLink + "?api=1&origin=" +
                    stripAccents(response.getRoutes().get(0).getLegs().get(0).getStartAddress()) +
                    "&origin_place_id=" + response.getGeoCodedWaypoints().get(0).getPlaceId() +
                    "&destination=" + stripAccents(response.getRoutes().get(0).getLegs().get(0).getEndAddress()) +
                    "&destination_place_id=" + response.getGeoCodedWaypoints().get(1).getPlaceId()
                    + "&region=cz&travelmode=");
        }
    }

    public static void getFare(RouteApiResponse response){
        if (response.getRoutes() != null && !response.getRoutes().isEmpty()
                && response.getRoutes().get(0).getFare() != null) {
            response.setFare(response.getRoutes().get(0).getFare().getText());
        }
    }

    private static String stripAccents(String address){
        return StringUtils.stripAccents(address)
                .replace(' ', '+')
                .replace(",", "%2");
    }

}
