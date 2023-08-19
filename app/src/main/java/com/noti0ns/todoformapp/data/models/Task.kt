package com.noti0ns.todoformapp.data.models

import androidx.room.*
import java.time.LocalDateTime

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String? = null,
    @ColumnInfo("is_done") val isDone: Boolean = false,
    @ColumnInfo("date_created") val dateCreated: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo("date_updated") val dateUpdated: LocalDateTime? = null,
    @ColumnInfo("due_date") val dueDate: LocalDateTime? = null,
    @ColumnInfo("date_finished") val dateFinished: LocalDateTime? = null,
) {
    constructor() : this(0, "")
}
