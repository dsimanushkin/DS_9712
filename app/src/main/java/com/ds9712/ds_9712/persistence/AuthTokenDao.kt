package com.ds9712.ds_9712.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ds9712.ds_9712.models.AuthToken

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(authToken: AuthToken): Long

    @Query("UPDATE auth_token SET auth_token = null WHERE account_id = :id")
    suspend fun nullifyToken(id: String): Int

    @Query("SELECT * FROM auth_token WHERE account_id = :id")
    suspend fun searchById(id: String): AuthToken?

}