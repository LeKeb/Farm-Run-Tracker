package com.kebstudios.farmrunhelper.data

import androidx.room.*

@Dao
interface FarmRunDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addFarmRun(farmRun: FarmRun): Long

    @Query("SELECT * FROM FarmRun")
    fun getAllFarmRuns(): List<FarmRun>

    @Delete
    fun deleteFarmRun(farmRun: FarmRun)
}