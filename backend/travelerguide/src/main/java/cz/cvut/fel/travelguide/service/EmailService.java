package cz.cvut.fel.travelguide.service;

import cz.cvut.fel.travelguide.interfaces.entity.EmailTemplateEntity;
import cz.cvut.fel.travelguide.interfaces.entity.PlaceEntity;
import cz.cvut.fel.travelguide.interfaces.entity.TripHistoryEntity;
import cz.cvut.fel.travelguide.interfaces.entity.UserInfoEntity;
import cz.cvut.fel.travelguide.interfaces.repository.EmailTemplateRepository;
import cz.cvut.fel.travelguide.interfaces.repository.PlaceRepository;
import cz.cvut.fel.travelguide.interfaces.repository.TripHistoryRepository;
import cz.cvut.fel.travelguide.interfaces.repository.UserInfoRepository;
import cz.cvut.fel.travelguide.interfaces.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserInfoRepository userInfoRepository;
    private final EmailTemplateRepository emailTemplateRepository;
    private final TripHistoryRepository tripHistoryRepository;
    private final PlaceRepository placeRepository;

    public EmailService(@Autowired @NotNull JavaMailSender javaMailSender,
                        @Autowired @NotNull UserInfoRepository userInfoRepository,
                        @Autowired @NotNull EmailTemplateRepository emailTemplateRepository,
                        @Autowired @NotNull TripHistoryRepository tripHistoryRepository,
                        @Autowired @NotNull PlaceRepository placeRepository){
        this.javaMailSender = javaMailSender;
        this.userInfoRepository = userInfoRepository;
        this.emailTemplateRepository = emailTemplateRepository;
        this.tripHistoryRepository = tripHistoryRepository;
        this.placeRepository = placeRepository;
    }

    public BaseResponse sendEmail(@NotNull String email) {
        String text = getText(email);
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        if (text == null) {
            return new BaseResponse("KO");
        }
        try {
            helper = new MimeMessageHelper(msg, true);
            helper.setTo(email);
            helper.setFrom("travel.guide@seznam.cz", "Průvodce Cestovatele");
            helper.setSubject("Detaily Vašeho výletu");
            helper.setText(text, true);
            javaMailSender.send(msg);
        } catch (MessagingException | UnsupportedEncodingException e) {
            return new BaseResponse("KO");
        }

        return new BaseResponse("OK");
    }

    @Transactional(readOnly = true)
    String getText(String email){
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.getOne(1);
        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(email);
        if (userInfoEntity != null) {
            List<TripHistoryEntity> tripHistoryEntities = tripHistoryRepository
                    .findByUserOrderByCreatedAtDesc(userInfoEntity);
            if (tripHistoryEntities != null && !tripHistoryEntities.isEmpty()){
                TripHistoryEntity tripHistoryEntity = tripHistoryEntities.get(0);
                PlaceEntity mainPlace = placeRepository.findByTripHistoryAndMainPlaceIsTrue(tripHistoryEntity);
                List<PlaceEntity> places = placeRepository.findByTripHistoryAndMainPlaceIsFalse(tripHistoryEntity);
                if (mainPlace != null) {
                    String template = setMainPlace(mainPlace, emailTemplateEntity);
                    template = setRoute(tripHistoryEntity, emailTemplateEntity, template);

                    if (places != null && !places.isEmpty()){
                        template = setPlaces(places, emailTemplateEntity, template);
                    } else {
                        template = template.replace("PLACES-TEMPLATE", "");
                    }
                    return template;
                }
            }
        }

        return null;
    }

    private String setMainPlace(PlaceEntity placeEntity, EmailTemplateEntity emailTemplateEntity){
        String template = emailTemplateEntity.getTemplate();
        String nameTemplate = placeEntity.getName() != null ?
                emailTemplateEntity.getPlaceNameTemplate().replace("PLACE-NAME", placeEntity.getName()) : "";
        String hoursTemplate = placeEntity.getHours() != null ?
                emailTemplateEntity.getPlaceHoursTemplate().replace("PLACE-HOURS", placeEntity.getHours()) : "";
        String addressTemplate = placeEntity.getAddress() != null ? emailTemplateEntity.getPlaceAddressTemplate()
                .replace("PLACE-ADDRESS", placeEntity.getAddress()) : "";
        String phonesTemplate = "";
        if (placeEntity.getPhones() != null) {
            String[] phones = placeEntity.getPhones().split(System.lineSeparator());
            for (String phone : phones) {
                String phoneTemplate = emailTemplateEntity.getPlacePhoneTemplate()
                        .replace("PLACE-PHONE-WITHOUT-PLUS", phone.replace("+", ""))
                        .replace("PLACE-PHONE", phone);
                phonesTemplate = phonesTemplate.concat(phoneTemplate);
            }
        }

        String websTemplate = "";
        if (placeEntity.getWebs() != null) {
            String[] webs = placeEntity.getWebs().split(System.lineSeparator());
            for (String web : webs) {
                String webTemplate = emailTemplateEntity.getPlaceWebTemplate()
                        .replace("PLACE-WEB-WITHOUT-HTTP",
                                web.replace("https://", "").replace("http://", ""))
                        .replace("PLACE-WEB", web);
                websTemplate = websTemplate.concat(webTemplate);
            }
        }

        return template.replace("PLACE_NAME_TEMPLATE", nameTemplate)
                .replace("PLACE_HOURS_TEMPLATE", hoursTemplate)
                .replace("PLACE_ADDRESS_TEMPLATE", addressTemplate)
                .replace("PLACE_PHONE_TEMPLATE", phonesTemplate)
                .replace("PLACE_WEB_TEMPLATE", websTemplate);
    }

    private String setRoute(TripHistoryEntity tripHistoryEntity, EmailTemplateEntity emailTemplateEntity, String template){
        String kmTemplate = tripHistoryEntity.getDistance() != null ?
                emailTemplateEntity.getKmTemplate().replace("SET-KM", tripHistoryEntity.getDistance()) : "";
        String timeTemplate = tripHistoryEntity.getDuration() != null ?
                emailTemplateEntity.getTimeTemplate().replace("SET-TIME", tripHistoryEntity.getDuration()) : "";
        String staticMapTemplate = tripHistoryEntity.getStaticMap() != null ? emailTemplateEntity.getStaticMapTemplate()
                .replace("STATIC-MAP", tripHistoryEntity.getStaticMap()) : "";
        String linkTemplate = tripHistoryEntity.getLink() != null ? emailTemplateEntity.getLinkTemplate()
                .replace("PLACE-MAP", tripHistoryEntity.getLink()) : "";

        return template.replace("KM_TEMPLATE", kmTemplate)
                .replace("TIME_TEMPLATE", timeTemplate)
                .replace("STATIC_MAP_TEMPLATE", staticMapTemplate)
                .replace("LINK_TEMPLATE", linkTemplate);
    }

    private String setPlaces(List<PlaceEntity> placeEntities, EmailTemplateEntity emailTemplateEntity, String template){
        String result = "";
        for (PlaceEntity placeEntity : placeEntities){
           String nameTemplate = placeEntity.getAddress() != null ?
                   emailTemplateEntity.getPlacesNameTemplate().replace("PLACES-NAME", placeEntity.getAddress()) : "";
           String hoursTemplate = placeEntity.getHours() != null ?
                   emailTemplateEntity.getPlacesHoursTemplate().replace("PLACES-HOURS", placeEntity.getHours()) : "";
           String linkTemplate = placeEntity.getLink() != null ?
                   emailTemplateEntity.getPlacesLinkTemplate().replace("PLACES-LINKS", placeEntity.getHours()) : "";

           String phonesTemplate = "";
           if (placeEntity.getPhones() != null) {
               String[] phones = placeEntity.getPhones().split(System.lineSeparator());
               for (String phone : phones) {
                   String phoneTemplate = emailTemplateEntity.getPlacesPhoneTemplate()
                           .replace("PLACES-PHONE-WITHOUT-PLUS", phone.replace("+", ""))
                           .replace("PLACES-PHONE", phone);
                   phonesTemplate = phonesTemplate.concat(phoneTemplate);
               }
           }

           String websTemplate = "";
           if (placeEntity.getWebs() != null) {
               String[] webs = placeEntity.getWebs().split(System.lineSeparator());
               for (String web : webs) {
                   String webTemplate = emailTemplateEntity.getPlacesWebTemplate()
                           .replace("PLACES-WEB-WITHOUT-HTTP",
                                   web.replace("https://", "").replace("http://", ""))
                           .replace("PLACES-WEB", web);
                   websTemplate = websTemplate.concat(webTemplate);
               }
           }

           result = result.concat(nameTemplate + hoursTemplate + phonesTemplate + websTemplate + linkTemplate);
        }
        String placesTemplate = emailTemplateEntity.getPlacesTemplate()
                .replace("PLACE-TEMPLATE", result);

        return template.replace("PLACES-TEMPLATE", placesTemplate);
    }

}
