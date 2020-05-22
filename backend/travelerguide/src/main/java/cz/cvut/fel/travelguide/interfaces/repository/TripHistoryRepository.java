package cz.cvut.fel.travelguide.interfaces.repository;

import cz.cvut.fel.travelguide.interfaces.entity.TripHistoryEntity;
import cz.cvut.fel.travelguide.interfaces.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripHistoryRepository extends JpaRepository<TripHistoryEntity, Integer> {
    List<TripHistoryEntity> findByUserOrderByCreatedAtDesc(UserInfoEntity user);
    TripHistoryEntity findByUserAndId(UserInfoEntity user, Integer id);
}
