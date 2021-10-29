package com.ds9712.ds_9712.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

const val AUTH_TOKEN_BUNDLE_KEY = "com.ds9712.ds_9712.models.AuthToken"

@JsonClass(generateAdapter = true)
@Entity(
    tableName = "auth_token",
    foreignKeys = [
        ForeignKey(
            entity = AccountProperties::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class AuthToken(
    @PrimaryKey
    @ColumnInfo(name = "account_id")
    var accountId: String = "",

    @Json(name = "auth_token")
    @ColumnInfo(name = "auth_token")
    var authToken: String? = null,

    @Json(name = "short_id")
    @ColumnInfo(name = "short_id")
    var shortId: String? = null
) : Parcelable