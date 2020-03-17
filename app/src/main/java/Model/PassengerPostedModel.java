package Model;

public class PassengerPostedModel  {

    private String post_id;
    private String ride_type;
    private String id;
    private String date;
    private String starting_point;
    private String ending_point;
    private String trip;
    private String schedule;

    public PassengerPostedModel() {
    }

    public PassengerPostedModel(String post_id, String ride_type, String id, String date
            , String starting_point, String ending_point, String trip, String schedule) {
        this.post_id = post_id;
        this.ride_type = ride_type;
        this.id = id;
        this.date = date;
        this.starting_point = starting_point;
        this.ending_point = ending_point;
        this.trip = trip;
        this.schedule = schedule;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getRide_type() {
        return ride_type;
    }

    public void setRide_type(String ride_type) {
        this.ride_type = ride_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
