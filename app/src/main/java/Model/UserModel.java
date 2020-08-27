package Model;

public class UserModel  {


    private String profile_img;
    private String uId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String city;
    private String cnic;
    private String vehicle_name;
    private String vehicle_number;
    private String car_type;
    private String car_doc;
    private Boolean profile_status = false;

    public UserModel() {
    }

    public UserModel(String profile_img,String uId, String name, String email, String phone, String password,
                     String city, String cnic, String vehicle_name, String vehicle_number, String car_type,String car_doc) {
        this.uId = uId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.city = city;
        this.cnic = cnic;
        this.vehicle_name = vehicle_name;
        this.vehicle_number = vehicle_number;
        this.car_type = car_type;
        this.profile_img = profile_img;
    }

    public String getCar_doc() {
        return car_doc;
    }

    public void setCar_doc(String car_doc) {
        this.car_doc = car_doc;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public Boolean getProfile_status() {
        return profile_status;
    }

    public void setProfile_status(Boolean profile_status) {
        this.profile_status = profile_status;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }
}
