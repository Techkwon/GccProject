package woogear.kwon.gccproject.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Place(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val thumbnail: String,
    @field:Embedded val description: PlaceDetail,
    val rate: Float
) : Parcelable {
    @IgnoredOnParcel
    var saveTime: Long = -1

    @Parcelize
    data class PlaceDetail(
        val imagePath: String,
        val subject: String,
        val price: Int
    ) : Parcelable
}