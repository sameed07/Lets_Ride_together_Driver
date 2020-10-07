package com.example.lets_ride_together_driver.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.lets_ride_together_driver.Activities.AuctionAmountSendActivity;
import com.example.lets_ride_together_driver.Activities.CustomerCall;
import com.example.lets_ride_together_driver.ChatStuff.MessageActivity;
import com.example.lets_ride_together_driver.Common.Common;
import com.example.lets_ride_together_driver.Helper.NotificationHelper;
import com.example.lets_ride_together_driver.NewModel.Token;
import com.example.lets_ride_together_driver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFireBaseMessaging extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        updateTokenToServer(s);
    }


    private void updateTokenToServer(final String refreshedToken) {

        if (FirebaseAuth.getInstance().getUid() != null) {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference tokens = db.getReference(Common.token_tbl);

            Token token = new Token(refreshedToken);

            tokens.child(FirebaseAuth.getInstance().getUid())
                    .setValue(token);
        }
    }


    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {

            Map<String, String> data = remoteMessage.getData();
            String customer = data.get("customer");
            String lat = data.get("lat");
            String lng = data.get("lng");
            String riderId = data.get("riderId");
            String avatarUri = data.get("avatarUri");
            System.err.println("msgrec" + lat + "_" + lng + "_" + customer);


            //   Toast.makeText(this, "recived message", Toast.LENGTH_SHORT).show();
            // Log.d("usama", remoteMessage.getNotification().getBody());
            //to send notification of latlng from rider app

            String isaution = data.get("isauction");
            String isdefault = data.get("default");
            String IsAfterAuction = data.get("IsAfterAuction");


            String riderFromLocation = data.get("riderFromLocation");
            String riderToLocation = data.get("riderToLocation");


            String amount = data.get("amount");


//new
            final String sented = remoteMessage.getData().get("sented");
            final String user = remoteMessage.getData().get("user");
            final String title = remoteMessage.getData().get("title");

            System.err.println("notifyme" + sented);

            if (title != null && title.equals("New Message")) {

                if (FirebaseAuth.getInstance().getUid() != null && sented.equals(FirebaseAuth.getInstance().getUid())) {
                    if (!FirebaseAuth.getInstance().getUid().equals(user)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            sendOreoNotification(remoteMessage);
                        } else {
                            sendNotification(remoteMessage);
                        }
                    }
                }


            }

            //for request
            if (title != null && title.equals("request")) {

                String reciver = remoteMessage.getData().get("reciver");
                String drivername = remoteMessage.getData().get("drivername");

                if (reciver.equals(FirebaseAuth.getInstance().getUid())) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        sendOreoNotificationforrequest(drivername, reciver);
                    } else {
                        sendNotificationforrequest(drivername, reciver);
                    }


                }


            }

//new


            if (isaution != null && isaution.equals("true")) {

                System.out.println("auction work1");


                Intent intent = new Intent(getBaseContext(), AuctionAmountSendActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("customer", customer);
                intent.putExtra("riderId", riderId);
                intent.putExtra("riderFromLocation", riderFromLocation);
                intent.putExtra("riderToLocation", riderToLocation);


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if (IsAfterAuction != null && IsAfterAuction.equals("true")) {


                Intent intent = new Intent(getBaseContext(), CustomerCall.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("customer", customer);
                intent.putExtra("riderId", riderId);
                intent.putExtra("avatarUri", avatarUri);
                intent.putExtra("IsAfterAuction", "true");
                intent.putExtra("amount", amount);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if (isdefault != null && isdefault.equals("true")) {

                Intent intent = new Intent(getBaseContext(), CustomerCall.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("customer", customer);
                intent.putExtra("riderId", riderId);
                intent.putExtra("avatarUri", avatarUri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }
    }


    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getOreoNotification(title, body, pendingIntent, defaultSound);

        notificationHelper.getManager().notify(1, builder.build());


    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();


        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        noti.notify(1, builder.build());
    }


    //for requset
    private void sendOreoNotificationforrequest(String Dname, String rece) {


        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),
                0, new Intent(), PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getOreoNotification("Request", Dname + " driver has started the ride so make sure you are at the start location", contentIntent, defaultSound);

        notificationHelper.getManager().notify(1, builder.build());


    }

    private void sendNotificationforrequest(String Dname, String rece) {


        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),
                0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.utube)
                .setContentTitle("Request")
                .setContentText(Dname + " driver has started the ride so make sure you are at the start location")
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());


    }


}
