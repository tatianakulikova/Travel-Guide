package cz.cvut.fel.travelguide.service;

import com.github.dozermapper.core.Mapper;
import cz.cvut.fel.travelguide.interfaces.api.RouteApiResponse;
import cz.cvut.fel.travelguide.interfaces.entity.TripHistoryEntity;
import cz.cvut.fel.travelguide.interfaces.response.RouteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class RouteService {

    private final ApiCommunicationService apiCommunication;
    private final TripHistoryService tripHistoryService;
    private final Mapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceService.class);

    RouteService(@NonNull @Autowired ApiCommunicationService apiCommunication,
                 @NotNull @Autowired TripHistoryService tripHistoryService,
                 @NotNull @Autowired PlaceService placeService,
                 @NonNull @Autowired Mapper mapper){
        this.apiCommunication = apiCommunication;
        this.tripHistoryService = tripHistoryService;
        this.mapper = mapper;
    }

    public RouteResponse getRoute(@NonNull String origin, @NotNull String coordinates,
                                  @NotNull String tripType, @NotNull String email, @NotNull String destination){
        LOGGER.debug("getRoute {} {} {}", origin, coordinates, tripType);
        Assert.notNull(origin, "origin cannot be null!");
        Assert.notNull(coordinates, "destination cannot be null!");
        Assert.notNull(tripType, "tripType cannot be null!");
        RouteResponse result = new RouteResponse();
        RouteApiResponse routeApiResponse;

        try {
            CompletableFuture<RouteApiResponse> future = apiCommunication
                        .callApiForRouteInfo(origin, coordinates, tripType);
            routeApiResponse = future.get(30, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e){
            LOGGER.debug("getRoute {} {} {} {}", origin, coordinates, tripType, e);
            return result;
        }

        if ("OK".equals(routeApiResponse.getStatus())){
            result = mapper.map(routeApiResponse, RouteResponse.class);
        }
        result.setRouteLink(result.getRouteLink() != null ? result.getRouteLink().concat(tripType)
                .concat("\" target=\"_blank\">Odkaz na Google Maps</a>") : null);

        tripHistoryService.createHistory(email, routeApiResponse, tripType, destination);

        return result;

    }

    public RouteResponse getRouteByHistory(@NotNull String email, @NotNull String coordinates, @NotNull String destination){
        LOGGER.debug("getRoteByHistory {} {} {}", email, coordinates, destination);
        TripHistoryEntity entity = tripHistoryService.getLastTripHistory(email);

        if (email != null){
            return getRoute(entity.getOriginPlace(), coordinates, entity.getTripMode(), email, destination);
        }

        return null;
    }

}
