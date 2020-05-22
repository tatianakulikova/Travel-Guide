package cz.cvut.fel.travelguide.interfaces.repository;

import cz.cvut.fel.travelguide.interfaces.entity.EmailTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity, Integer> {
}
