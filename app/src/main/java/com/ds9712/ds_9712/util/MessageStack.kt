package com.ds9712.ds_9712.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.IgnoredOnParcel
import timber.log.Timber
import java.lang.IndexOutOfBoundsException

class MessageStack: ArrayList<StateMessage>() {

    @IgnoredOnParcel
    private val _stateMessage: MutableLiveData<StateMessage?> = MutableLiveData()

    @IgnoredOnParcel
    val stateMessage: LiveData<StateMessage?>
        get() = _stateMessage

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        for (element in elements) {
            add(element)
        }
        return true // Always return true
    }

    override fun add(element: StateMessage): Boolean {
        if (this.contains(element)) { // Prevent duplicate errors added to stack
            return false
        }
        val transaction = super.add(element)
        if (this.size == 1) {
            setStateMessage(stateMessage = element)
        }
        return transaction
    }

    override fun removeAt(index: Int): StateMessage {
        try {
            val transaction = super.removeAt(index)
            if (this.size > 0) {
                setStateMessage(stateMessage = this[0])
            } else {
                Timber.d("stack is empty: ")
                setStateMessage(null)
            }
            return transaction
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
        return StateMessage(
            Response(
                message = "Does nothing",
                statusCode = -2,
                requestCode = -1
            )
        ) // This does nothing
    }

    private fun setStateMessage(stateMessage: StateMessage?) {
        _stateMessage.value = stateMessage
    }

}