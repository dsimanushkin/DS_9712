package com.ds9712.ds_9712.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ds9712.ds_9712.models.AccountProperties

@Dao
interface AccountPropertiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReplace(accountProperties: AccountProperties): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(accountProperties: AccountProperties): Long

    @Query("SELECT * FROM account_properties WHERE id = :id")
    suspend fun searchById(id: String): AccountProperties

    @Query("SELECT * FROM account_properties WHERE username = :username")
    suspend fun searchByUsername(username: String): AccountProperties?

    @Query("UPDATE account_properties SET username = :username WHERE id = :id")
    suspend fun updateAccountProperties(id: String, username: String)

}