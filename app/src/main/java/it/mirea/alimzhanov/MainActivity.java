package it.mirea.alimzhanov;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.hardware.Camera;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_CAMERA = 5 ;
    private static final String NOTIFICATION_CHANNEL_ID = "5";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        Button buttonNotification= findViewById(R.id.button_notification);
        buttonNotification.setOnClickListener(v -> showNotification());
        Button buttonCamera = findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(v -> checkSelfPermission());
    }


    void checkSelfPermission (){
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permissionStatus== PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (permissionStatus== PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMISSION_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else {
            return;
        }
    }

    void showCamera () {
        Camera camera = Camera.open();
        SurfaceView surfaceView = findViewById(R.id.surfaceView); // Ищем элемент на экране, описанном в R.layout.activity_main с id = surfaceView
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview(); // запускаем превью камеры.
    }


    private void createNotificationChannel() {
        CharSequence name = "Notifications";
        String description = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new
                NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    private void showNotification() {
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Практическая работа 4")
                .setContentText("Алимжанов Билолидин ИКБО-07-20")
                .setStyle(new NotificationCompat.BigTextStyle());
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        Random random = new Random(); // notificationId уникален для каждого уведомления. Используем рандом, для генерации.
        notificationManager.notify(random.nextInt(), builder.build()); //показываем уведомление
    }
}
