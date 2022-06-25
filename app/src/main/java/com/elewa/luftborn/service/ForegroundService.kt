package com.elewa.luftborn.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.elewa.luftborn.ui.MainActivity
import com.elewa.luftborn.R
import com.elewa.luftborn.data.local.AppDatabase
import com.elewa.luftborn.data.model.TaskModel
import com.elewa.luftborn.util.Constants
import kotlinx.coroutines.*

class ForegroundService : Service() {
    private val CHANNEL_ID = "ForegroundService"
    companion object {
        fun startService(context: Context, message: String,taskId: Int) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("message", message)
            startIntent.putExtra("task_id", taskId)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        val input = intent?.getStringExtra("message")
        val taskId = intent?.getIntExtra("task_id",0)
        val model = TaskModel(taskId!!,"","",0)
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Running..")
            .setContentText(input)
            .setSmallIcon(R.drawable.download_icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        CoroutineScope(Dispatchers.Default).launch {

            for (n in 1..100) {
                println(n)
                delay(1000)
                var rand = (3..7).random()
                if (model.status!! + rand > 100 || n == 100) {
                    model.status = 100
                } else {
                    model.status = model.status!! + rand
                }

                AppDatabase.getInstance(applicationContext).taskDao.update(model.id, model.status)

                if (model.status == 100) {
                    stopSelf();
                    break
                }
            }

            Constants.DOWNLOADFLAG = false

        }

//        stopSelf();
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Luftborn Running Task",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(this)
    }
}