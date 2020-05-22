package cz.cvut.fel.travelguide.service.util;

import cz.cvut.fel.travelguide.interfaces.api.PlaceApiResponse;
import cz.cvut.fel.travelguide.interfaces.enums.WeekEnum;
import cz.cvut.fel.travelguide.interfaces.response.PlaceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlaceUtil {

    private static String routeStaticMapUrl;
    private static String routeMapLink;
    private static String routeApiKey;

    @Value("${route.static.map.url}")
    public void setRouteStaticMapUrl(String staticMapUrl){
        routeStaticMapUrl = staticMapUrl;
    }

    @Value("${route.map.link.search}")
    public void setRouteMapLink(String mapLink){
        routeMapLink = mapLink;
    }

    @Value("${route.api.key}")
    public void setRouteApiKey(String apiKey){
        routeApiKey = apiKey;
    }

    public static void getWorkingHours(PlaceApiResponse.Item item){
        if (item.getOpeningHours() != null && !item.getOpeningHours().isEmpty()){
            PlaceApiResponse.Item.OpeningHours openingHours = item.getOpeningHours().get(0);
            PlaceApiResponse.Item.WorkingHours workingHours = new PlaceApiResponse.Item.WorkingHours();
            workingHours.setIsWorking(openingHours.getIsOpen());
            String workingTime = "";
            for (int i = 0; i < openingHours.getStructured().size(); i++){
                String line = "";
                        String[] days = openingHours.getStructured().get(i).getRecurrence()
                        .replaceFirst("FREQ:DAILY;BYDAY:", "")
                        .split(",");
                for (String day : days){
                    if (!line.equals("")){
                        line = line.concat(", ");
                    }
                    line = line.concat(WeekEnum.fromCode(day));
                }
                line = line.concat(":");
                int k = openingHours.getStructured().size() == openingHours.getText().size() ? i : 0;
                line = line.concat(openingHours.getText().get(k)
                        .replaceFirst(":", "/").split("/")[1]);
                workingTime = workingTime.concat(line).concat(System.lineSeparator());
            }
            workingTime = workingTime.substring(0, workingTime.length() - 1);
            workingHours.setWorkingTime(workingTime);
            item.setWorkingHours(workingHours);
        }
    }

    public static void getPhones(PlaceApiResponse.Item item){
        if (item.getContacts() != null && !item.getContacts().isEmpty()
                && item.getContacts().get(0).getPhone() != null) {
            String phones = "";
            for (String i : item.getContacts().get(0).getPhone().stream()
                    .map(PlaceApiResponse.Item.Contacts.BaseContact::getValue)
                    .distinct().collect(Collectors.toList())){
                phones = phones.concat(i).concat(System.lineSeparator());
            }
            phones = phones.substring(0, phones.length() - 1);
            item.setPhones(phones);
        }
    }

    public static void getWebPages(PlaceApiResponse.Item item){
        if (item.getContacts() != null && !item.getContacts().isEmpty()
                && item.getContacts().get(0).getWww()!= null) {
            String webPages = "";
            for (String i : item.getContacts().get(0).getWww().stream()
                    .map(PlaceApiResponse.Item.Contacts.BaseContact::getValue)
                    .distinct().collect(Collectors.toList())){
                webPages = webPages.concat(i).concat(System.lineSeparator());
            }
            webPages = webPages.substring(0, webPages.length() - 1);
            item.setWebPages(webPages);
        }
    }

    public static void getEmails(PlaceApiResponse.Item item){
        if (item.getContacts() != null && !item.getContacts().isEmpty()
                && item.getContacts().get(0).getEmail() != null) {
            String emails = "";
            for (String i : item.getContacts().get(0).getEmail().stream()
                    .map(PlaceApiResponse.Item.Contacts.BaseContact::getValue)
                    .distinct().collect(Collectors.toList())){
                emails = emails.concat(i).concat(System.lineSeparator());
            }
            emails = emails.substring(0, emails.length() - 1);
            item.setEmails(emails);
        }
    }

    public static void getMapPicture(PlaceApiResponse.Item item){
        if (item.getPosition() != null) {
            item.setMapPicture(routeStaticMapUrl + "?zoom=15&size=500x400&language=cs&markers="
                    + item.getPosition().getLat() + "," + item.getPosition().getLng() +
                    "&key=" + routeApiKey);
        }
    }

    public static void getMapLink(PlaceApiResponse.Item item){
        if (item.getPosition() != null) {
            item.setMapLink("<a href=\"" + routeMapLink + "?api=1&query=" +
                    item.getPosition().getLat() + "," + item.getPosition().getLng() +
                    "\" target=\"_blank\">Odkaz na Google Maps</a>");
        }
    }

    public static List<PlaceResponse.Response> removeDuplicatedObjects(List<PlaceResponse.Response> responses){
        List<PlaceResponse.Response> result = new ArrayList<>();
        for (PlaceResponse.Response response : responses){
            if (!contains(result, response)) {
                result.add(response);
            }
        }

        return result;
    }

    private static boolean contains(List<PlaceResponse.Response> responses, PlaceResponse.Response response){
        for (PlaceResponse.Response resp : responses){
            if ((resp.getTitle() != null && response.getTitle() != null &&
                    resp.getTitle().equals(response.getTitle())) &&
                    ((resp.getLng() != null && response.getLng() != null &&
                            resp.getLng().equals(response.getLng()) && resp.getLat() != null
                            && response.getLat() != null && resp.getLat().equals(response.getLat()))
                    || (resp.getPostalCode() != null && response.getPostalCode() != null
                            && resp.getPostalCode().equals(response.getPostalCode())))){
                return true;
            }
        }

        return false;
    }
}
