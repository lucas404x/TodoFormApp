package com.noti0ns.todoformapp.data.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Entity
@Parcelize
data class Task(
    @PrimaryKey var id: Int,
    @ColumnInfo var title: String,
    @ColumnInfo var description: String? = null,
    @ColumnInfo("is_done") var isDone: Boolean = false,
    @ColumnInfo("date_created") var dateCreated: Instant = Instant.now(),
    @ColumnInfo("date_updated") var dateUpdated: Instant? = null,
    @ColumnInfo("due_date") var dueDate: Instant? = null,
    @ColumnInfo("date_finished") var dateFinished: Instant? = null,
) : Parcelable {
    constructor() : this(0, "")
}
