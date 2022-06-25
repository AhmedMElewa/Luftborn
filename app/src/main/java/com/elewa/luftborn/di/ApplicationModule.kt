package com.elewa.luftborn.di

import android.content.Context
import com.elewa.luftborn.data.local.AppDatabase
import com.elewa.luftborn.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideItemDao(@ApplicationContext appContext: Context): TaskDao {
        return AppDatabase.getInstance(appContext).taskDao
    }
}