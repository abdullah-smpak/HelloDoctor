package com.hellodoc.abdullah.hellodoc.Service;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hellodoc.abdullah.hellodoc.Appoint_Status;
import com.hellodoc.abdullah.hellodoc.Common.Common;
import com.hellodoc.abdullah.hellodoc.Model.Request;
import com.hellodoc.abdullah.hellodoc.R;

public class ListenApp extends Service implements ChildEventListener {
   FirebaseDatabase db;
   DatabaseReference request;

    public ListenApp() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        request = db.getReference("Request");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        request.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        Request request = dataSnapshot.getValue(Request.class);
        showNotiofication(dataSnapshot.getKey(),request);
    }

    private void showNotiofication(String key, Request request) {
        Intent intent = new Intent(getBaseContext(), Appoint_Status.class);
        intent.putExtra("userPhone",request.getPhone());
        PendingIntent contentintent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Hello Doctor")
                .setContentTitle("Hello Doctor")
                .setContentInfo("Your Appointment Status Was Updated")
                .setContentText("Appointment #" +key+ "was updated to status "+ Common.convertCodestatus(request.getStatus()))
                .setContentIntent(contentintent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);


        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
