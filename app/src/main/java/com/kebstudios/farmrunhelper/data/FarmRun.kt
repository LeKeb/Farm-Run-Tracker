package com.kebstudios.farmrunhelper.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class FarmRun(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val timestamp: Long

) : Serializable