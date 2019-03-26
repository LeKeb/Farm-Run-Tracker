package com.kebstudios.farmrunhelper.data

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Database(entities = [FarmRun::class, FarmRunItem::class], version = 1)
abstract class FarmRunDatabase : RoomDatabase() {

    abstract fun farmRunDao(): FarmRunDao

    abstract fun farmRunItemDao(): FarmRunItemDao

    companion object {

        private var instance: FarmRunDatabase? = null

        fun getInstance(ctx: Context): FarmRunDatabase {
            synchronized(this) {
                if (instance == null) {
                    val path = Environment.getExternalStorageDirectory().path + "/kebstudios/farm_run_app/"
                    val fileName = "database.db"
                    val file = File(path)
                    if (!file.exists()) {
                        file.mkdirs()
                    }
                    Log.i("FarmRunDatabase", "Database path: $path")
                    Log.i("FarmRunDatabase", "Opening database")
                    instance = Room.databaseBuilder(ctx, FarmRunDatabase::class.java, path + fileName).build()
                }

                return instance!!
            }
        }

        fun destroyInstance() {
            synchronized(this) {
                if (instance != null) {
                    Log.i("FarmRunDatabase", "Closing database")

                    instance!!.close()
                    instance = null
                }

            }
        }
    }

}
