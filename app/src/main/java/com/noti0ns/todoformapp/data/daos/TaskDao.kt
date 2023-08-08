package com.noti0ns.todoformapp.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.noti0ns.todoformapp.data.models.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task WHERE id = :id")
    suspend fun get(id: Int): Task

    @Query("SELECT * FROM Task")
    suspend fun getAll(): List<Task>

    @Insert
    suspend fun save(task: Task): Task

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(id: Int)
}