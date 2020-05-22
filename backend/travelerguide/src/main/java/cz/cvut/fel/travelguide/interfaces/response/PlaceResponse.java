package cz.cvut.fel.travelguide.interfaces.response;

import java.util.List;

public class PlaceResponse {

    private List<Response> responses;

    public static class Response {
        private String title;
        private String addressLabel;
        private String city;
        private String postalCode;
        private Float lat;
        private Float lng;
        private String phones;
        private String webPages;
        private String emails;
        private String workingTime;
        private String mapPicture;
        private String mapLink;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddressLabel() {
            return addressLabel;
        }

        public void setAddressLabel(String addressLabel) {
            this.addressLabel = addressLabel;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public Float getLat() {
            return lat;
        }

        public void setLat(Float lat) {
            this.lat = lat;
        }

        public Float getLng() {
            return lng;
        }

        public void setLng(Float lng) {
            this.lng = lng;
        }

        public String getPhones() {
            return phones;
        }

        public void setPhones(String phones) {
            this.phones = phones;
        }

        public String getWebPages() {
            return webPages;
        }

        public void setWebPages(String webPages) {
            this.webPages = webPages;
        }

        public String getEmails() {
            return emails;
        }

        public void setEmails(String emails) {
            this.emails = emails;
        }

        public String getWorkingTime() {
            return workingTime;
        }

        public void setWorkingTime(String workingTime) {
            this.workingTime = workingTime;
        }

        public String getMapPicture() {
            return mapPicture;
        }

        public void setMapPicture(String mapPicture) {
            this.mapPicture = mapPicture;
        }

        public String getMapLink() {
            return mapLink;
        }

        public void setMapLink(String mapLink) {
            this.mapLink = mapLink;
        }

        @Override
        public String toString(){
            String result = null;
            if (getAddressLabel() != null){
                result = getAddressLabel();
                if (getWebPages() != null){
                    result = result + System.lineSeparator() + getWebPages();
                }
                if (getPhones() != null){
                    result = result + System.lineSeparator() + getPhones();
                }
                if (getWorkingTime() != null){
                    result = result + System.lineSeparator() + getWorkingTime();
                }
                if (getMapLink() != null){
                    result = result + System.lineSeparator() + getMapLink();
                }
            }
             return result;
        }

    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

}
