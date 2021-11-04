package com.ds9712.ds_9712.ui.main.state

import com.ds9712.ds_9712.util.StateEvent

sealed class MainStateEvent: StateEvent {
    object GetCurrentQuestionEvent : MainStateEvent() {
        override fun errorInfo(): String {
            return "Error getting current question."
        }

        override fun toString(): String {
            return "GetCurrentQuestionEvent"
        }
    }

    data class SolveQuestionEvent(
        val userKey: String
    ): MainStateEvent() {
        override fun errorInfo(): String {
            return "Unable to send key."
        }

        override fun toString(): String {
            return "SolveQuestionEvent"
        }
    }
}