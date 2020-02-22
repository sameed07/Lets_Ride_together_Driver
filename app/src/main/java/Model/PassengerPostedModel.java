package Model;

public class PassengerPostedModel  {
    private String passenger_name;
    private String vehicle_type;
    private String image;
    private String date;
    private String time;
    private String starting_lat;
    private String starting_lng;
    private String ending_lat;
    private String ending_lng;
    private String trip_detail;

    public PassengerPostedModel() {
    }

    public PassengerPostedModel(String driver_name, String car_type,
                           String date, String time, String trip_detail, String post_lat, String post_lng) {
        this.passenger_name = driver_name;
        this.vehicle_type = car_type;
        this.date = date;
        this.time = time;
        this.trip_detail = trip_detail;
        this.starting_lat = post_lat;
        this.starting_lng = post_lng;
    }

    public PassengerPostedModel(String passenger_name, String vehicle_type, String image, String date,
                                String time, String starting_lat, String starting_lng, String ending_lat,
                                String ending_lng, String trip_detail) {

        this.passenger_name = passenger_name;
        this.vehicle_type = vehicle_type;
        this.image = image;
        this.date = date;
        this.time = time;
        this.starting_lat = starting_lat;
        this.starting_lng = starting_lng;
        this.ending_lat = ending_lat;
        this.ending_lng = ending_lng;
        this.trip_detail = trip_detail;
    }

    public String getPassenger_name() {
        return passenger_name;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStarting_lat() {
        return starting_lat;
    }

    public void setStarting_lat(String starting_lat) {
        this.starting_lat = starting_lat;
    }

    public String getStarting_lng() {
        return starting_lng;
    }

    public void setStarting_lng(String starting_lng) {
        this.starting_lng = starting_lng;
    }

    public String getEnding_lat() {
        return ending_lat;
    }

    public void setEnding_lat(String ending_lat) {
        this.ending_lat = ending_lat;
    }

    public String getEnding_lng() {
        return ending_lng;
    }

    public void setEnding_lng(String ending_lng) {
        this.ending_lng = ending_lng;
    }

    public String getTrip_detail() {
        return trip_detail;
    }

    public void setTrip_detail(String trip_detail) {
        this.trip_detail = trip_detail;
    }
}
