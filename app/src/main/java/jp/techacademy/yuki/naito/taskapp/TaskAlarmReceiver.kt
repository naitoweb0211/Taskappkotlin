package jp.techacademy.yuki.naito.taskapp

import android.R
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import java.util.*
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.Realm.getApplicationContext

private const val TAG = "MyBroadcastReceiver"
const val NOTIFICATION_ID = 0
class TaskAlarmReceiver : BroadcastReceiver() {
    val CHANNEL_ID = "sample"
    private val am: AlarmManager? = null
    private val pending: PendingIntent? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MyBroadcastReceiverReceive", "MyBroadcastReceiverReceive")
       /* val pendingResult: PendingResult = goAsync()
        val asyncTask = Work(pendingResult, intent)
        asyncTask.execute()*/
        //val requestCode = intent.getIntExtra("RequestCode", -1)
        var mTask = intent.getIntExtra(EXTRA_TASK, -1)
        // 通知の設定を行う
        var builder = NotificationCompat.Builder(context, "default")
        builder.setSmallIcon(R.drawable.ic_dialog_alert)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_dialog_alert))
        builder.setWhen(System.currentTimeMillis())
        builder.setDefaults(Notification.DEFAULT_ALL)
        builder.setAutoCancel(true)
        // EXTRA_TASKからTaskのidを取得して、 idからTaskのインスタンスを取得する
        val taskId = intent!!.getIntExtra(EXTRA_TASK, -1)
        val realm = Realm.getDefaultInstance()
        val task = realm.where(Task::class.java).equalTo("id", taskId).findFirst()

        builder.setContentTitle(task!!.title)
        builder.setContentText(task!!.contents)
        // 通知をタップしたらアプリを起動するようにする
     /*   val startAppIntent = Intent(context, MainActivity::class.java)
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        val pendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, 0)
        builder.setContentIntent(pendingIntent)*/
        val channelId = "default"
/*        val dataFormat = SimpleDateFormat("HH:mm:ss", Locale.JAPAN)
        val cTime: String = dataFormat.format(currentTime)*/

        // メッセージ　+ 11:22:331

        // メッセージ　+ 11:22:331
        val message = "時間になりました。"


        // Notification　Channel 設定


        // Notification　Channel 設定
        val channel = NotificationChannel(
            "default", "通知",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = message


        val notificationManager =
            //                (NotificationManager)context.getSystemService(NotificationManager.class);
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
/*
        val builder = NotificationCompat.Builder(context, channelId)
        builder.setSmallIcon(R.drawable.btn_star)
        builder.setContentTitle("My notification")
        builder.setContentText(message)
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        //builder.setContentIntent(intent)
        builder.setAutoCancel(true)*/

        val notificationManagerCompat = NotificationManagerCompat.from(context)

        // 通知
        //    notificationManagerCompat.notify(R.string.ok, builder.build())
        // 通知を表示する
        notificationManagerCompat.notify(task!!.id, builder.build())
        realm.close()


    }
}