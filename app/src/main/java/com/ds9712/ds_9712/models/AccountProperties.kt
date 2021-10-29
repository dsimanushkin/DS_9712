package com.ds9712.ds_9712.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Entity(
    tableName = "account_properties",
    indices = [
        Index(value = ["id"], unique = true)
    ],
    primaryKeys = ["id"]
)
@Parcelize
data class AccountProperties(

    @Json(name = "id")
    @ColumnInfo(name = "id")
    var id: String,

    @Json(name = "username")
    @ColumnInfo(name = "username")
    var username: String,

    @Json(name = "short_id")
    @ColumnInfo(name ="short_id")
    var shortId: String

) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as AccountProperties

        if (id != other.id) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}