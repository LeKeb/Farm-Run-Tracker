package com.kebstudios.farmrunhelper.data

import androidx.room.*

@Dao
interface FarmRunItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addFarmRunItem(item: FarmRunItem)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addFarmRunItems(items: List<FarmRunItem>)

    @Query("SELECT * FROM FarmRunItem WHERE farmRunID LIKE :id")
    fun getFarmRunItemsByFarmRunId(id: Long): List<FarmRunItem>

    @Query("SELECT * FROM FarmRunItem WHERE patchType LIKE :type")
    fun getFarmRunItemsByPatchType(type: String): List<FarmRunItem>

    @Query("SELECT * FROM FarmRunItem WHERE place LIKE :place AND patchType LIKE :type")
    fun getFarmRunItemsByPatchLocationAndType(place: String, type: String): List<FarmRunItem>

    @Query("SELECT * FROM FarmRunItem WHERE harvestType LIKE :type")
    fun getFarmRunItemsByHarvestType(type: String): List<FarmRunItem>

    @Delete
    fun deleteFarmRunItem(item: FarmRunItem)
}