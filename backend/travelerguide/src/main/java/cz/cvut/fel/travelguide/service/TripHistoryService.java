package cz.cvut.fel.travelguide.service;

import cz.cvut.fel.travelguide.interfaces.api.RouteApiResponse;
import cz.cvut.fel.travelguide.interfaces.entity.TripHistoryEntity;
import cz.cvut.fel.travelguide.interfaces.entity.UserInfoEntity;
import cz.cvut.fel.travelguide.interfaces.repository.TripHistoryRepository;
import cz.cvut.fel.travelguide.interfaces.repository.UserInfoRepository;
import cz.cvut.fel.travelguide.interfaces.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Service
public class TripHistoryService {

    private final UserInfoRepository userInfoRepository;
    private final TripHistoryRepository tripHistoryRepository;
    private final PlaceService placeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceService.class);

    TripHistoryService(@NotNull @Autowired UserInfoRepository userInfoRepository,
                       @NotNull @Autowired PlaceService placeService,
                       @NotNull @Autowired TripHistoryRepository tripHistoryRepository){
        this.userInfoRepository = userInfoRepository;
        this.placeService = placeService;
        this.tripHistoryRepository = tripHistoryRepository;
    }

    @Transactional
    public void createHistory(@NotNull String email, @NotNull RouteApiResponse response,
                              @NotNull String tripMode, @NotNull String destination){
        LOGGER.debug("getUserInfo {} {}", email, response);
        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(email);
        TripHistoryEntity entity = new TripHistoryEntity();

        if (userInfoEntity != null && notNull(response)) {
            entity.setUser(userInfoEntity);
            entity.setTripMode(tripMode);
            entity.setOriginPlace(response.getRoutes().get(0).getLegs().get(0).getStartAddress());
            entity.setDestinationPlace(destination);
            entity.setDistance(response.getTotalDistance());
            entity.setDuration(response.getTotalTime());
            entity.setLink(response.getRouteLink() != null ?
                    response.getRouteLink().replace("<a href=\"", "") : null);
            entity.setStaticMap(response.getRoutePicture());
            entity.setCreatedAt(new Date());
            tripHistoryRepository.save(entity);

            if (!userInfoEntity.getFeedback()) {
                if (userInfoEntity.getFeedbackIteration() <= 0) {
                    userInfoEntity.setFeedback(true);
                    userInfoEntity.setFeedbackIteration(5);
                } else {
                    userInfoEntity.setFeedbackIteration(userInfoEntity.getFeedbackIteration() - 1);
                }
            }

            placeService.getInfoByPlaceName(destination, email);

            userInfoRepository.save(userInfoEntity);
        }
    }

    @Transactional(readOnly = true)
    public TripHistoryEntity getLastTripHistory(@NotNull String email){
        LOGGER.debug("getLastTripHistory {}", email);
        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(email);

        if (userInfoEntity != null){
            List<TripHistoryEntity> tripHistoryEntities = tripHistoryRepository
                    .findByUserOrderByCreatedAtDesc(userInfoEntity);

            if (tripHistoryEntities != null && !tripHistoryEntities.isEmpty()) {
                return tripHistoryEntities.get(0);
            }
        }

        return null;
    }

    @Transactional
    public BaseResponse saveFeedback(String email, String feedback, Integer id, Boolean feedbackStop){
        LOGGER.debug("saveFeedback {} {} {}", email, feedback, id);
        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(email);
        String result = "KO";

        if (userInfoEntity != null){
            if (feedbackStop){
                userInfoEntity.setFeedback(false);
                userInfoRepository.save(userInfoEntity);
            } else {
                TripHistoryEntity tripHistoryEntity = tripHistoryRepository
                        .findByUserAndId(userInfoEntity, id);
                if (tripHistoryEntity != null) {
                    tripHistoryEntity.setFeedback(feedback);
                    tripHistoryRepository.save(tripHistoryEntity);
                    result = "OK";
                }
            }
        }

        return new BaseResponse(result);
    }

    private boolean notNull(RouteApiResponse response){
        return response.getRoutes() != null && !response.getRoutes().isEmpty()
                && response.getRoutes().get(0).getLegs() != null && !response.getRoutes().get(0).getLegs().isEmpty()
                && response.getRoutes().get(0).getLegs().get(0).getStartAddress() != null
                && response.getRoutes().get(0).getLegs().get(0).getEndAddress() != null;
    }

}
