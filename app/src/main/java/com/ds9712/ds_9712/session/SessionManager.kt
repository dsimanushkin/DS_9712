package com.ds9712.ds_9712.session

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ds9712.ds_9712.models.AuthToken
import com.ds9712.ds_9712.persistence.AuthTokenDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    private val authTokenDao: AuthTokenDao,
    val application: Application
){

    private val _cachedToken = MutableLiveData<AuthToken?>()

    val cachedToken: LiveData<AuthToken?> get() = _cachedToken

    fun login(newValue: AuthToken) {
        setValue(newValue)
    }

    fun logout() {

        GlobalScope.launch(Dispatchers.IO) {
            var errorMessage: String? = null
            try {
                // Removing token from local DB
                cachedToken.value!!.accountId.let {
                    authTokenDao.nullifyToken(it)
                }
            } catch (e: CancellationException) {
                errorMessage = e.message
            }
            catch (e: Exception) {
                errorMessage += "\n ${e.message}"
            }
            finally {
                errorMessage?.let {
                }
                setValue(null)
            }
        }
    }

    fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Dispatchers.Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue
            }
        }
    }
}