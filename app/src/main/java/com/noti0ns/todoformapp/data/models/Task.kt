package com.noti0ns.todoformapp.data.models

import androidx.room.*
import java.time.Instant

@Entity
data class Task(
    @PrimaryKey var id: Int,
    @ColumnInfo var title: String,
    @ColumnInfo var description: String?,
    @ColumnInfo("is_done") var isDone: Boolean = false,
    @ColumnInfo("date_created") var dateCreated: Instant = Instant.now(),
    @ColumnInfo("date_updated") var dateUpdated: Instant? = null,
    @ColumnInfo("date_to_finish") var dateToFinish: Instant? = null,
    @ColumnInfo("date_finished") var dateFinished: Instant? = null,
)
