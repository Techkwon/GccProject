package woogear.kwon.gccproject.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.room.OnConflictStrategy.*
import woogear.kwon.gccproject.model.Place

@Dao
interface FavoritePlaceDao {
    @Query("SELECT * FROM Place")
    fun allPlaces(): DataSource.Factory<Int, Place>

    @Query("SELECT * FROM Place ORDER BY saveTime ASC")
    fun favoritePlacesByTimeAsc(): LiveData<List<Place>>

    @Insert(onConflict = REPLACE)
    suspend fun save(place: Place)

    @Delete
    suspend fun delete(place: Place)

    @Insert(onConflict = REPLACE)
    fun insert(places: List<Place>)
}