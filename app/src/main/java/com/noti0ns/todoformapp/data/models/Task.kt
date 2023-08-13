package com.noti0ns.todoformapp.data.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Entity
@Parcelize
data class Task(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String? = null,
    @ColumnInfo("is_done") val isDone: Boolean = false,
    @ColumnInfo("date_created") val dateCreated: Instant = Instant.now(),
    @ColumnInfo("date_updated") val dateUpdated: Instant? = null,
    @ColumnInfo("due_date") val dueDate: Instant? = null,
    @ColumnInfo("date_finished") val dateFinished: Instant? = null,
) : Parcelable {
    constructor() : this(0, "")
}
