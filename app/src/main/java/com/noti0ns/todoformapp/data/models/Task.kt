package com.noti0ns.todoformapp.data.models

import androidx.room.*
import java.time.Instant

@Entity
data class Task(
    @PrimaryKey var id: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT) var title: String,
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT) var description: String?,
    @ColumnInfo("is_done", typeAffinity = ColumnInfo.INTEGER) var isDone: Boolean = false,
    @ColumnInfo(
        "date_created",
        typeAffinity = ColumnInfo.INTEGER
    ) var dateCreated: Instant = Instant.now(),
    @ColumnInfo("date_updated", typeAffinity = ColumnInfo.INTEGER) var dateUpdated: Instant? = null,
    @ColumnInfo(
        "date_to_finish",
        typeAffinity = ColumnInfo.INTEGER
    ) var dateToFinish: Instant? = null,
    @ColumnInfo(
        "date_finished",
        typeAffinity = ColumnInfo.INTEGER
    ) var dateFinished: Instant? = null,
)
