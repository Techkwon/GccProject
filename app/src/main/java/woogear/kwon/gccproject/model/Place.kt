package woogear.kwon.gccproject.model

import androidx.room.*
import java.io.Serializable

@Entity
data class Place(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val thumbnail: String,
    @field:Embedded val description: PlaceDetail,
    val rate: String
) : Serializable {
    var saveTime: Long = -1

    data class PlaceDetail(
        val imagePath: String,
        val subject: String,
        val price: Int
    ) : Serializable
}