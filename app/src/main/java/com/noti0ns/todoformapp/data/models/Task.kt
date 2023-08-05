package com.noti0ns.todoformapp.data.models

import java.time.Instant

data class Task(
    val id: Int,
    val title: String,
    val dateCreated: Instant = Instant.now(),
    val dateUpdated: Instant? = null,
    val description: String = "",
    val isDone: Boolean = false,
    val targetFinishDate: Instant? = null,
)
