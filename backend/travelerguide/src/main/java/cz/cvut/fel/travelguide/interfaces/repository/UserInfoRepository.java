package cz.cvut.fel.travelguide.interfaces.repository;

import cz.cvut.fel.travelguide.interfaces.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {
    UserInfoEntity findByEmail(String email);
}
