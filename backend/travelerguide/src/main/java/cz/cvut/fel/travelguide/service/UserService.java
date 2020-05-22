package cz.cvut.fel.travelguide.service;

import com.github.dozermapper.core.Mapper;
import cz.cvut.fel.travelguide.interfaces.entity.TripHistoryEntity;
import cz.cvut.fel.travelguide.interfaces.repository.TripHistoryRepository;
import cz.cvut.fel.travelguide.interfaces.repository.UserInfoRepository;
import cz.cvut.fel.travelguide.interfaces.entity.UserInfoEntity;
import cz.cvut.fel.travelguide.interfaces.response.UserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final TripHistoryRepository tripHistoryRepository;
    private final Mapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceService.class);

    UserService(@NotNull @Autowired UserInfoRepository userInfoRepository,
                @NotNull @Autowired TripHistoryRepository tripHistoryRepository,
                @NotNull @Autowired Mapper mapper){
        this.userInfoRepository = userInfoRepository;
        this.tripHistoryRepository = tripHistoryRepository;
        this.mapper = mapper;
    }

    @Transactional
    public UserInfoResponse getUserInfo(@NotNull String email){
        LOGGER.debug("getUserInfo {}", email);
        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(email);
        UserInfoResponse result;

        if (userInfoEntity != null){
            List<TripHistoryEntity> tripHistoryEntities = tripHistoryRepository
                    .findByUserOrderByCreatedAtDesc(userInfoEntity);
            result = mapper.map(userInfoEntity, UserInfoResponse.class);
            result.setStatus("200");
            if (tripHistoryEntities != null && !tripHistoryEntities.isEmpty()) {
                result.setUseHistory(true);
                result.setDestination(tripHistoryEntities.get(0).getDestinationPlace());
                result.setFeedbackId(tripHistoryEntities.get(0).getId());
            } else {
                result.setUseHistory(false);
            }
        } else {
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setEmail(email);
            userInfoRepository.save(userInfoEntity);
            result = mapper.map(userInfoEntity, UserInfoResponse.class);
            result.setUseHistory(false);
            result.setStatus("201");
        }

        return result;
    }

}
