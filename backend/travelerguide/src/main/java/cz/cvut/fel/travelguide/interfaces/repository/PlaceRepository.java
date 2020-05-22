package cz.cvut.fel.travelguide.interfaces.repository;

import cz.cvut.fel.travelguide.interfaces.entity.PlaceEntity;
import cz.cvut.fel.travelguide.interfaces.entity.TripHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Integer> {
    PlaceEntity findByTripHistoryAndMainPlaceIsTrue(TripHistoryEntity tripHistory);
    List<PlaceEntity> findByTripHistoryAndMainPlaceIsFalse(TripHistoryEntity tripHistory);
    void deleteByTripHistory(TripHistoryEntity tripHistory);
}
