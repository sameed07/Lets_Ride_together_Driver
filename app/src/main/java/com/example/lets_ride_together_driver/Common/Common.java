package com.example.lets_ride_together_driver.Common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.lets_ride_together_driver.Model.UserModel;
import com.example.lets_ride_together_driver.NewModel.TripHistory;
import com.example.lets_ride_together_driver.NewModel.UberDriver;
import com.example.lets_ride_together_driver.NewModel.User;
import com.example.lets_ride_together_driver.Remote.FCMClient;
import com.example.lets_ride_together_driver.Remote.IFCMService;
import com.example.lets_ride_together_driver.Remote.IGoogleApi;
import com.example.lets_ride_together_driver.Remote.RetrofitClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class Common {


    //DriverInformation
    public static final String driver_tbl = "DriversOnline";
    public static final String driver_tbl_online = "DriversOnline";
    public static final String user_driver_tbl = "Users/Drivers";
    public static final String user_rider_tbl = "Users/Passengers";
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
    public static Location mLastLocation = null;
    public static UberDriver currentUberDriver;
    public static TripHistory triphistoryStatic;
    public static double base_fare = 2.55;
    public static double time_rate = 0.35;
    public static double distance_rate = 1.75;
    public static String user_field = "usr";
    public static String pwd_field = "pwd";


    public static String auction_list_tbl = "Auction_Driver_List";
    public static String auction_list_result_tbl = "Auction_List_Result";


    public static UserModel currentUser;


    public static User currentDriver;
//    public static TripHistory triphistoryStatic;


    public static double formulaPrice(double km, int min) {
        return (base_fare + (time_rate * min) + distance_rate * km);
    }


    public static IGoogleApi getGoogleAPI() {

        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }

    public static IFCMService getFCMService() {

        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }


    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Curction address", strReturnedAddress.toString());
            } else {
                Log.w("My Coction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Cun address", "Canont get Address!");
        }
        return strAdd;
    }


    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(),
                    location.getLongitude());

            return p1;
        } catch (Exception e) {
            return new LatLng(0.0, 0.0);
        }
    }


}
