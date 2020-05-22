package cz.cvut.fel.travelguide.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.cvut.fel.travelguide.interfaces.request.BaseRequest;
import cz.cvut.fel.travelguide.interfaces.response.BaseResponse;
import cz.cvut.fel.travelguide.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
class ChatbotCommunicationController {

    private final PlaceService placeService;
    private final RouteService routeService;
    private final UserService userService;
    private final TripHistoryService tripHistoryService;
    private final EmailService emailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatbotCommunicationController.class);

    ChatbotCommunicationController(@NonNull @Autowired PlaceService placeService,
                                   @NotNull @Autowired RouteService routeService,
                                   @NotNull @Autowired UserService userService,
                                   @NotNull @Autowired TripHistoryService tripHistoryService,
                                   @NotNull @Autowired EmailService emailService){
        this.placeService = placeService;
        this.routeService = routeService;
        this.userService = userService;
        this.tripHistoryService = tripHistoryService;
        this.emailService = emailService;
    }

    @RequestMapping(value = "api", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object chatbotCommunication(@RequestBody @NotNull @Valid BaseRequest request) {
        LOGGER.debug("chatbotCommunication {}", request);

        switch (request.getRequestType()) {
            case USER_GET_API: // create account or log in
                return userService.getUserInfo(request.getEmail());
            case EMAIL_API: // send email
                return emailService.sendEmail(request.getEmail());
            case PLACE_API: // find concrete monument
                return placeService.getInfoByPlaceName(request.getPlaceName(), request.getEmail());
            case PLACES_API: // find places around monument
                return placeService.getInfoByCategory(request.getCoordinates(), request.getCategory(), request.getEmail());
            case FEEDBACK_API: // save feedback about trip
                return tripHistoryService.saveFeedback(request.getEmail(), request.getFeedback(),
                        request.getFeedbackId(), request.getFeedbackStop());
            case ROUTE_API: // plan route to monument
                if (request.getUseHistory()){
                    return routeService.getRouteByHistory(request.getEmail(),
                            request.getCoordinates(), request.getDestination());
                }
                return routeService.getRoute(request.getOrigin(), request.getCoordinates(),
                        request.getTripType().getValue(), request.getEmail(), request.getDestination());
        }

        return new BaseResponse("OK");
    }

}
