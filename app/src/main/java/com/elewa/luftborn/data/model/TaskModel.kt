package com.elewa.luftborn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task")
data class TaskModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val date: String,
    var status: Int = 0
)
