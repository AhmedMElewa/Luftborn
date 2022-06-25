package com.elewa.luftborn.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elewa.luftborn.data.model.TaskModel
import com.elewa.luftborn.util.Constants.DATABASE_NAME

@Database(entities = [TaskModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}