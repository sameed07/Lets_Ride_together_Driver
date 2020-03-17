package Model;

public class DriverPost {

    public DriverPost() {
    }


    private String postKey;
    private String id;
    private String price;
    private String date;
    private String ride_type;
    private String schedule;
    private String seats;
    private String trip;
    private String starting_point,ending_point;

    public DriverPost(String postKey, String id, String price, String date, String ride_type,
                      String schedule, String seats, String trip, String starting_point, String ending_point) {
        this.postKey = postKey;
        this.id = id;
        this.price = price;
        this.date = date;
        this.ride_type = ride_type;
        this.schedule = schedule;
        this.seats = seats;
        this.trip = trip;
        this.starting_point = starting_point;
        this.ending_point = ending_point;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getStarting_point() {
        return starting_point;
    }

    public void setStarting_point(String starting_point) {
        this.starting_point = starting_point;
    }

    public String getEnding_point() {
        return ending_point;
    }

    public void setEnding_point(String ending_point) {
        this.ending_point = ending_point;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRide_type() {
        return ride_type;
    }

    public void setRide_type(String ride_type) {
        this.ride_type = ride_type;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }



    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

}
