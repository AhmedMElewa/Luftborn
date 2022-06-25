package com.elewa.luftborn.data.local

import androidx.room.*
import com.elewa.luftborn.data.model.TaskModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(taskList: TaskModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(taskList: List<TaskModel>)

    @Query("SELECT * FROM task")
    fun get(): Flow<List<TaskModel>>

    @Query("update task set status=:status where id=:id")
    suspend fun update(id : Int, status: Int)

    @Delete
    suspend fun delete(task: TaskModel)

    @Query("DELETE FROM task")
    suspend fun clear()
}