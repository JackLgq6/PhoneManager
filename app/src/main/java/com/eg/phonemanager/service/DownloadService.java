package com.eg.phonemanager.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.eg.phonemanager.Activity.HomeActivity;
import com.eg.phonemanager.R;

import java.io.File;

/**
 * Created by gqliu on 17-8-17.
 */

public class DownloadService extends Service {

    private DownloadTask downloadTask;

    private String downloadUrl;

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            //When the download is successful, close the foreground service notification,
            //and create a download success notification
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            //When the download is failed, close the foreground service notification,
            //and create a download fail notification
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class DownloadBinder extends Binder {

        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification("Downloading...", 0));
                Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    //if user cancel the download then need to delete the file
                    //and close off notifications
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .getParent();
                    File file = new File(fileName + directory);
                    if (file.exists()) {
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

     private NotificationManager getNotificationManager() {
         return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
     }

     private Notification getNotification(String title, int progress) {
         Intent intent = new Intent(this, HomeActivity.class);
         PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
         NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
         builder.setSmallIcon(R.drawable.download);
         builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.download));
         builder.setContentIntent(pi);
         builder.setContentTitle(title);
         if (progress > 0) {
             //When progress is greater than or equal to zero, download progress is displayed
             builder.setContentText(progress + "%");
             builder.setProgress(100, progress, false);
         }
         return builder.build();
     }
}
