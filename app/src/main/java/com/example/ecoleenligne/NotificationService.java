package com.example.ecoleenligne;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.ecoleenligne.Model.Eleve;
import com.example.ecoleenligne.Model.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service {

    private FirebaseAuth mauth;
    private FirebaseFirestore db;
    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "FIREBASEOC";
    private String TAG = "NotificationService";
    private int Taille = 0;

    public NotificationService() {

    }


    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");

        this.GetNotificationFromDB();
        this.GetReminderFromDB();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void GetNotificationFromDB() {

        mauth = FirebaseAuth.getInstance();

        String uid = mauth.getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Notification").document("user " + uid);


        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult() != null) {
                                if (task.getResult().getData() != null) {
                                    Notification notification = task.getResult().toObject(Notification.class);
                                    ArrayList<String> listnotif = (ArrayList<String>) notification.getNotifs();
                                    Taille = listnotif.size();
                                    Log.d(TAG, "onComplete: Taille :" + Taille);
                                }
                            }
                        }

                        docRef.addSnapshotListener(MetadataChanges.INCLUDE,new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }

                                if (snapshot != null && snapshot.exists()) {
                                    Log.d(TAG, "Current data: " + snapshot.getData());
                                    Notification notification = snapshot.toObject(Notification.class);
                                    ArrayList<String> listnotif = (ArrayList<String>) notification.getNotifs();
                                    if (listnotif.size() > Taille) {
                                        Log.d(TAG, "onEvent: " + Taille);
                                        Taille = listnotif.size();
                                        String notif = listnotif.get(listnotif.size() - 1);
                                        sendVisualNotification(notif);
                                    }

                                } else {
                                    Log.d(TAG, "Current data: null");
                                }
                            }
                        });

                    }
                });


    }

    private void GetReminderFromDB() {
        mauth = FirebaseAuth.getInstance();
        String uid = mauth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("Users").document("user " + uid).collection("ReminderNotification").document("notification");
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult() != null) {
                                if (task.getResult().getData() != null) {
                                    String date = "";
                                    String Heure = "";

                                    if (task.getResult().get("dateRappel") != null) {
                                        date = task.getResult().get("dateRappel").toString();
                                    }
                                    if (task.getResult().get("heureRappel") != null) {
                                        Heure = task.getResult().get("dateRappel").toString();
                                    }

                                    Log.d(TAG, "date de recuperation :" + date + " heure" + Heure);

                                }
                            }
                        }

                        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }

                                if (snapshot != null && snapshot.exists()) {
                                    Log.d(TAG, "Current data: " + snapshot.getData());
                                    String date = "";
                                    String Heure = "";
                                    Boolean shown = false;
                                    if (snapshot.get("dateRappel") != null) {
                                        date = snapshot.get("dateRappel").toString();
                                        Log.d(TAG, "onEvent: " + date);
                                    }

                                    if (snapshot.get("heureRappel") != null) {
                                        Heure = snapshot.get("heureRappel").toString();
                                        Log.d(TAG, "onEvent: " + Heure);
                                    }

                                    if (snapshot.get("ShownNotif") != null) {
                                        shown = snapshot.getBoolean("ShownNotif");


                                    }
                                    if (date.length() > 0 && Heure.length() > 0 && !shown) {
                                        docRef.update("ShownNotif", true)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Log.d(TAG, "onComplete: "+snapshot.get("dateRappel").toString()+"  "+snapshot.get("heureRappel").toString());
                                                            Log.d(TAG, "onComplete: lancementsendVisualnotif");
                                                            sendVisualNotificationReminder(snapshot.get("dateRappel").toString(), snapshot.get("heureRappel").toString());
                                                        }
                                                    }
                                                });

                                    }



                                } else {
                                    Log.d(TAG, "Current data: null");
                                }
                            }
                        });

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: erreur de fetch");
            }
        });




    }


    private void sendVisualNotification(String messageBody) {

        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("RecommandationFragment", "goToRecommandation");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notification_title));
        inboxStyle.addLine(messageBody);

        // 3 - Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_chat_black_24dp)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_title))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // 7 - Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }


    private void sendVisualNotificationReminder(String date, String heure) {

        Intent intent = new Intent(getApplicationContext(), ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        String dateString = date + " " + heure + ":0";
        Log.d(TAG, "sendVisualNotificationReminder: date de notif " + dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Log.d(TAG, "sendVisualNotificationReminder: dateFormat"+dateFormat);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "sendVisualNotificationReminder: " + e);
            e.printStackTrace();
        }


        Date startDate = new Date();
        Date endDate = convertedDate;

        Log.d(TAG, "sendVisualNotificationReminder: convertDate " + convertedDate);
        Log.d(TAG, "sendVisualNotificationReminder: startDate " + startDate);

        long duration = endDate.getTime() - startDate.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        Log.d(TAG, "sendVisualNotificationReminder: " + diffInMinutes);
        long diffInMilli = diffInSeconds * 1000;
        Log.d(TAG, "sendVisualNotificationReminder: " + diffInMilli);
        long timeNow = System.currentTimeMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeNow + diffInMilli, pendingIntent);

    }


    public void onDestroy() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("com.example.ecoleenligne");
        intent.putExtra("yourvalue", "torestore");
        sendBroadcast(intent);
    }
}
