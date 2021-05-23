package vijay.app.dona.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.work.Worker
import androidx.work.WorkerParameters
import vijay.app.dona.R
import vijay.app.dona.ui.MainActivity
import java.util.*

class NotifyWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private val TAG = "NotifyWorker"

    override fun doWork(): Result {
        Log.d(TAG, "doWork: ${Calendar.getInstance().time}")

        triggerNotification()
        return Result.success()
    }

    private fun triggerNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_TAG,
                importance
            )

            channel.description = "This notification will help you to create the Task."
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 1, intent,
            FLAG_UPDATE_CURRENT
        )

        val notificationTitle = "Hey Morning!!"
        val notificationText = "Let's start the day with some Listing 😉"

        val notificationBuilder = NotificationCompat.Builder(
            applicationContext, Constants.NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_app_icon)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager: NotificationManagerCompat =
            NotificationManagerCompat.from(applicationContext)
        manager.notify(1, notificationBuilder.build())
    }
}