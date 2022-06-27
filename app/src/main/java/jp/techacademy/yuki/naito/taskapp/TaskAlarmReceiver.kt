package jp.techacademy.yuki.naito.taskapp

import android.R
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import io.realm.Realm
import java.util.*


private const val TAG = "MyBroadcastReceiver"

class TaskAlarmReceiver : BroadcastReceiver() {
    val CHANNEL_ID = "sample"
    var currentTime: Long = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MyBroadcastReceiverReceive", "MyBroadcastReceiverReceive")
       /* val pendingResult: PendingResult = goAsync()
        val asyncTask = Work(pendingResult, intent)
        asyncTask.execute()*/
        val requestCode = intent.getIntExtra("RequestCode", 0)
        this.currentTime = intent.getLongExtra("DATE", 0)
        Log.d("日付", (intent.getLongExtra("DATE", 0)).toString())
        val pendingIntent = PendingIntent.getActivity(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = "default"

        val dataFormat = SimpleDateFormat("HH:mm:ss", Locale.JAPAN)
        val cTime: String = dataFormat.format(currentTime)

        // メッセージ　+ 11:22:331

        // メッセージ　+ 11:22:331
        val message = "時間になりました。 $cTime"


        // Notification　Channel 設定


        // Notification　Channel 設定
        val channel = NotificationChannel(
            "default", "通知",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = message


        val notificationManager =
            //                (NotificationManager)context.getSystemService(NotificationManager.class);
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.btn_star)
            .setContentTitle("My notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setWhen(currentTime)

        val notificationManagerCompat = NotificationManagerCompat.from(context)

        // 通知

        // 通知
        notificationManagerCompat.notify(R.string.ok, builder.build())

    }
}