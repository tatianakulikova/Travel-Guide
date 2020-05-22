package cz.cvut.fel.travelguide.service;

import com.github.dozermapper.core.Mapper;
import cz.cvut.fel.travelguide.interfaces.api.PlaceApiResponse;
import cz.cvut.fel.travelguide.interfaces.entity.PlaceEntity;
import cz.cvut.fel.travelguide.interfaces.entity.TripHistoryEntity;
import cz.cvut.fel.travelguide.interfaces.entity.UserInfoEntity;
import cz.cvut.fel.travelguide.interfaces.enums.CategoryEnum;
import cz.cvut.fel.travelguide.interfaces.repository.PlaceRepository;
import cz.cvut.fel.travelguide.interfaces.repository.TripHistoryRepository;
import cz.cvut.fel.travelguide.interfaces.repository.UserInfoRepository;
import cz.cvut.fel.travelguide.interfaces.response.BaseResponse;
import cz.cvut.fel.travelguide.interfaces.response.PlaceResponse;
import cz.cvut.fel.travelguide.service.util.PlaceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final ApiCommunicationService apiCommunication;
    private final PlaceRepository placeRepository;
    private final TripHistoryRepository tripHistoryRepository;
    private final UserInfoRepository userInfoRepository;
    private final Mapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceService.class);

    PlaceService(@NonNull @Autowired ApiCommunicationService apiCommunication,
                 @NotNull @Autowired PlaceRepository placeRepository,
                 @NotNull @Autowired TripHistoryRepository tripHistoryRepository,
                 @NotNull @Autowired UserInfoRepository userInfoRepository,
                 @NonNull @Autowired Mapper mapper){
        this.apiCommunication = apiCommunication;
        this.placeRepository = placeRepository;
        this.tripHistoryRepository = tripHistoryRepository;
        this.userInfoRepository = userInfoRepository;
        this.mapper = mapper;
    }

    public PlaceResponse getInfoByPlaceName(@NonNull String placeName, String email){
        LOGGER.debug("getInfoByPlaceName {} {}", placeName, email);
        Assert.notNull(placeName, "placeName cannot be null!");
        PlaceResponse result = new PlaceResponse();
        List<PlaceResponse.Response> responses = new ArrayList<>();
        PlaceApiResponse placeApiResponse;

        try {
            CompletableFuture<PlaceApiResponse> completableFuture = apiCommunication
                    .callApiForPlaceInfoDiscover(placeName);
            placeApiResponse = completableFuture.get(30, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e){
            LOGGER.debug("getInfoByPlaceNameError {} {}", placeName, e);
            return result;
        }

        List<PlaceResponse.Response> finalResponses = responses;
        placeApiResponse.getItems().stream()
                .filter(results -> "CZE".equals(results.getAddress().getCountryCode()))
                .forEach(placeResponse -> finalResponses.add(mapper.map(placeResponse, PlaceResponse.Response.class)));

        responses = PlaceUtil.removeDuplicatedObjects(responses);

        if (email != null){
            savePlaces(responses.subList(0, 1), email, Boolean.TRUE);
        }

        result.setResponses(responses);
        return result;
    }

    public BaseResponse getInfoByCategory(@NotNull String coordinates, @NotNull String category, @NotNull String email){
        LOGGER.debug("getInfoByCategory {} {} {}", coordinates, category, email);
        Assert.notNull(coordinates, "coordinates cannot be null!");
        Assert.notNull(category, "category cannot be null!");
        Assert.notNull(email, "email cannot be null!");
        List<PlaceResponse.Response> responses = new ArrayList<>();
        PlaceApiResponse placeApiResponse;
        final String[] result = {""};

        try {
            CompletableFuture<PlaceApiResponse> completableFuture = apiCommunication
                    .callApiForPlaceInfoBrowse(coordinates, CategoryEnum.fromValue(category));
            placeApiResponse = completableFuture.get(30, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e){
            LOGGER.debug("getInfoByCategoryError {} {} {}", coordinates, category, e);
            return null;
        }

        placeApiResponse.getItems().stream()
                .filter(results -> "CZE".equals(results.getAddress().getCountryCode()))
                .forEach(placeResponse -> responses.add(mapper.map(placeResponse, PlaceResponse.Response.class)));

        List<PlaceResponse.Response> results = responses.stream()
                .filter(response ->  response.getAddressLabel() != null
                && (response.getPhones() != null || response.getWebPages() != null))
                .collect(Collectors.toList());

        results = PlaceUtil.removeDuplicatedObjects(results);

        savePlaces(results, email, Boolean.FALSE);

        results.forEach(response -> result[0] = result[0] + response.toString()
                        + System.lineSeparator() + System.lineSeparator());

        if (!result[0].equals("")){
            result[0] = result[0].substring(0, result[0].length() - 2);
        }

        return new BaseResponse(result[0]);
    }

    @Transactional
    void savePlaces(List<PlaceResponse.Response> responses, String email, Boolean mainPlace){
        TripHistoryEntity tripHistoryEntity = findTrip(email);
        if (tripHistoryEntity != null){
            for (PlaceResponse.Response response : responses){
                PlaceEntity placeEntity = new PlaceEntity();
                placeEntity.setAddress(response.getAddressLabel());
                placeEntity.setHours(response.getWorkingTime());
                placeEntity.setMainPlace(mainPlace);
                placeEntity.setName(response.getTitle());
                placeEntity.setPhones(response.getPhones());
                placeEntity.setTripHistory(tripHistoryEntity);
                placeEntity.setWebs(response.getWebPages());
                placeRepository.save(placeEntity);
            }
        }
    }

    @Transactional(readOnly = true)
     TripHistoryEntity findTrip(String email){
        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(email);
        List<TripHistoryEntity> tripHistoryEntities = tripHistoryRepository
                .findByUserOrderByCreatedAtDesc(userInfoEntity);
        if (tripHistoryEntities != null && !tripHistoryEntities.isEmpty()){
            return tripHistoryEntities.get(0);
        }

        return null;
    }

    @Scheduled(cron = "0 0 2 1/1 * ?")
    @Transactional
    public void removeOldPlaces() {
        List<UserInfoEntity> userInfoEntities = userInfoRepository.findAll();
        for (UserInfoEntity userInfoEntity : userInfoEntities){
            List<TripHistoryEntity> tripHistoryEntities = tripHistoryRepository
                    .findByUserOrderByCreatedAtDesc(userInfoEntity);
            for (TripHistoryEntity tripHistoryEntity : tripHistoryEntities.subList(1, tripHistoryEntities.size())){
                placeRepository.deleteByTripHistory(tripHistoryEntity);
            }
        }
    }

}
