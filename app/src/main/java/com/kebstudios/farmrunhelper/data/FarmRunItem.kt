package com.kebstudios.farmrunhelper.data

import androidx.room.Entity
import androidx.room.ForeignKey
import java.io.Serializable

@Entity(
    primaryKeys = ["farmRunID", "patchType", "place"],
    foreignKeys = [ForeignKey(entity = FarmRun::class, parentColumns = ["id"], childColumns = ["farmRunID"], onDelete = ForeignKey.CASCADE)])
data class FarmRunItem(
    val farmRunID: Long,
    val patchType: String, //Herb, Tree, Flower
    val place: String, //Patch location
    val harvestType: String,
    val harvestAmount: Int,
    val hesporiSeeds: Int,
    val plantedType: String,
    val compostType: String,
    val farmingLevel: Int,
    val secateurs: Boolean,
    val patchStatus: String
) : Serializable