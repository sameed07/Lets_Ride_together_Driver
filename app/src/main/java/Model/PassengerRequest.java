package Model;

public class PassengerRequest {


    private String passenger_id;
    private String post_id;
    private String sender_id;
    private String status;

    public PassengerRequest() {
    }

    public PassengerRequest(String passenger_id, String post_id, String sender_id, String status) {
        this.passenger_id = passenger_id;
        this.post_id = post_id;
        this.sender_id = sender_id;
        this.status = status;
    }

    public String getPassenger_id() {
        return passenger_id;
    }

    public void setPassenger_id(String passenger_id) {
        this.passenger_id = passenger_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
