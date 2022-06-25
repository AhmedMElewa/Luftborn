package com.elewa.luftborn.data.repository

import com.elewa.luftborn.data.local.TaskDao
import com.elewa.luftborn.data.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    fun getTasks(): Flow<List<TaskModel>> {
        return taskDao.get();
    }

    suspend fun insert(tasks: List<TaskModel>){
        taskDao.insert(tasks)
    }
}