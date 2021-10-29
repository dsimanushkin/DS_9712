package com.ds9712.ds_9712.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ds9712.ds_9712.models.AccountProperties
import com.ds9712.ds_9712.models.AuthToken

@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getAuthTokenDao(): AuthTokenDao
    abstract fun getAccountPropertiesDao(): AccountPropertiesDao
//    abstract fun getBlogPostDao(): BlogPostDao

    companion object {
        const val DATABASE_NAME = "kyrie_app_db"
    }
}