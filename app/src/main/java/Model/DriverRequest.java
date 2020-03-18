package Model;

public class DriverRequest {

    private String driver_id;
    private String post_id;
    private String sender_id;
    private String status;

    public DriverRequest() {
    }

    public DriverRequest(String driver_id, String post_id, String sender_id, String status) {
        this.driver_id = driver_id;
        this.post_id = post_id;
        this.sender_id = sender_id;
        this.status = status;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
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
