package Common;

import android.location.Location;

import Model.UserModel;
import Remote.IGoogleApi;
import Remote.RetrofitClient;

public class Common {

    public static String currentUser;
    public static Location mLastLocation = null;
    public static final String driver_tbl = "Drivers";
    public static final String user_driver_tbl = "DriverInformation";
    public static final String user_rider_tbl = "RiderInformation";
    public static final String pickup_request_tbl = "PickupRequest";
    public static final String token_tbl = "Tokens";
    public static final int PICK_IMAGE = 9999;

    public static final String driver_TripHistory_tbl = "TripHistoryDriver";
    public static final String driver_TripDecline_tbl = "declinedTrips";
    public static final String driver_TripAccept_tbl = "acceptedTrips";
    public static final String driver_TripCompleted_tbl = "completedTrips";
    public static final String baseURL = "https://maps.googleapis.com";
    public static final String fcmURL = "https://fcm.googleapis.com";
    public static final String MY_SHARED_PREF_NAME = "MyData";
    public static UserModel currentDriver;
//    public static TripHistory triphistoryStatic;
    public static double base_fare = 2.55;
    public static double time_rate = 0.35;
    public static double distance_rate = 1.75;
    public static String user_field = "usr";
    public static String pwd_field = "pwd";


    public static String auction_list_tbl = "Auction_Driver_List";
    public static String auction_list_result_tbl = "Auction_List_Result";

    public static double formulaPrice(double km, int min) {
        return (base_fare + (time_rate * min) + distance_rate * km);
    }


    public static IGoogleApi getGoogleAPI() {

        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
//
//    public static IFCMService getFCMService() {
//
//        return FCMClient.getClient(fcmURL).create(IFCMService.class);
//    }

}
