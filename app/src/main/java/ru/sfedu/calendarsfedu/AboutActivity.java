package ru.sfedu.calendarsfedu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import static ru.sfedu.calendarsfedu.MainActivity.NaviGroup;
import static ru.sfedu.calendarsfedu.MainActivity.NaviName;


public class AboutActivity extends AppCompatActivity {

    public static int inter = 0;
    ImageView about;
    private static final int NOTIFY_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.AToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("");




        about = (ImageView)findViewById(R.id.aboutimage);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                //startActivity(intent);

                Context context = getApplicationContext();

                Intent notificationIntent = new Intent(context, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context,
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                Resources res = context.getResources();
                Notification.Builder builder = new Notification.Builder(context);


                builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.whitelogo)

                        // большая картинка
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle("Скоро пара!")
                        .setContentText("Пара через: " + inter + " минут");

                Notification notification = builder.build();

                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFY_ID, notification);

                notification.defaults = Notification.DEFAULT_ALL;
                Uri ringURI =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notification.sound = ringURI;
                notification.ledARGB = Color.RED;
                notification.ledOffMS = 0;
                notification.ledOnMS = 1;
                notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;

                inter++;
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
