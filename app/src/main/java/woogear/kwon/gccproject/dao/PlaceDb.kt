package woogear.kwon.gccproject.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import woogear.kwon.gccproject.model.Place

@Database(entities = [Place::class], version = 1)
abstract class PlaceDb : RoomDatabase() {
    abstract fun placeDao(): FavoritePlaceDao

    companion object {
        private var instance: PlaceDb? = null

        fun getInstance(context: Context): PlaceDb {
            return instance ?: synchronized(this) {
                instance ?: buildDb(context).also { instance = it }
            }
        }

        private fun buildDb(context: Context): PlaceDb {
            return Room.databaseBuilder(context.applicationContext, PlaceDb::class.java, "PlaceDataBase")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}