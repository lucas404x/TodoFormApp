package com.noti0ns.todoformapp.data.models

import androidx.room.*
import java.time.Instant

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String? = null,
    @ColumnInfo("is_done") val isDone: Boolean = false,
    @ColumnInfo("date_created") val dateCreated: Instant = Instant.now(),
    @ColumnInfo("date_updated") val dateUpdated: Instant? = null,
    @ColumnInfo("due_date") val dueDate: Instant? = null,
    @ColumnInfo("date_finished") val dateFinished: Instant? = null,
) {
    constructor() : this(0, "")
}
