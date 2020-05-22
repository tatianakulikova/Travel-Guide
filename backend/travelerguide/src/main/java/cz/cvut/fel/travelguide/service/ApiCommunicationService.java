package cz.cvut.fel.travelguide.service;

import cz.cvut.fel.travelguide.interfaces.api.PlaceApiResponse;
import cz.cvut.fel.travelguide.interfaces.api.RouteApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;

@Service
class ApiCommunicationService {

    @Value("${places.discover.api.url}")
    private String placeApiUrlDiscover;
    @Value("${places.browse.api.url}")
    private String placeApiUrlBrowse;
    @Value("${places.api.key}")
    private String placeApiKey;
    @Value("${route.api.url}")
    private String routeApiUrl;
    @Value("${route.api.key}")
    private String routeApiKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCommunicationService.class);
    private final RestTemplate restTemplate;


    ApiCommunicationService(@NonNull @Autowired RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    CompletableFuture<PlaceApiResponse> callApiForPlaceInfoDiscover(@NonNull String placeName){
        LOGGER.debug("callApiForPlaceInfoDiscover {}", placeName);
        Assert.notNull(placeName, "placeName cannot be null!");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(placeApiUrlDiscover)
                .queryParam("apiKey", placeApiKey)
                .queryParam("at", "50.073658,14.418540")
                .queryParam("in", "countryCode:CZE")
                .queryParam("q", "");

        ResponseEntity<PlaceApiResponse> response = restTemplate.exchange(
                builder.toUriString() + placeName, HttpMethod.GET, prepareHttpEntity(), PlaceApiResponse.class);

        return (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null)
                    ? null : CompletableFuture.completedFuture(response.getBody());
    }

    CompletableFuture<PlaceApiResponse> callApiForPlaceInfoBrowse(@NotNull String coordinates,
                                                                  @NotNull String category){
        LOGGER.debug("callApiForPlaceInfoBrowse {} {}", coordinates, category);
        Assert.notNull(coordinates, "coordinates cannot be null!");
        Assert.notNull(category, "category cannot be null!");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(placeApiUrlBrowse)
                .queryParam("apiKey", placeApiKey)
                .queryParam("at", coordinates)
                .queryParam("limit", "6")
                .queryParam("categories", category);

        ResponseEntity<PlaceApiResponse> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, prepareHttpEntity(), PlaceApiResponse.class);

        return (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null)
                ? null : CompletableFuture.completedFuture(response.getBody());
    }

    CompletableFuture<RouteApiResponse> callApiForRouteInfo(@NonNull String origin,
                                                            @NotNull String destination,
                                                            @NotNull String tripType){
        LOGGER.debug("callApiForRouteInfo {} {} {}", origin, destination, tripType);
        Assert.notNull(origin, "origin cannot be null!");
        Assert.notNull(destination, "destination cannot be null!");
        Assert.notNull(tripType, "tripType cannot be null!");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(routeApiUrl)
                .queryParam("key", routeApiKey)
                .queryParam("mode", tripType)
                .queryParam("language", "cs")
                .queryParam("origin", "");

        ResponseEntity<RouteApiResponse> response = restTemplate.exchange(
                builder.toUriString() + origin + "&destination=" + destination,
                HttpMethod.GET, prepareHttpEntity(), RouteApiResponse.class);

        return (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null)
                ? null : CompletableFuture.completedFuture(response.getBody());
    }

    private HttpEntity<?> prepareHttpEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }

}
