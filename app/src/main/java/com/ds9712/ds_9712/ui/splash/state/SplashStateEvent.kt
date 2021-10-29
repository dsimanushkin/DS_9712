package com.ds9712.ds_9712.ui.splash.state

import com.ds9712.ds_9712.util.StateEvent

sealed class SplashStateEvent: StateEvent {
    object CheckPreviousAuthEvent : SplashStateEvent() {
        override fun errorInfo(): String {
            return "Error checking for previously authenticated user."
        }

        override fun toString(): String {
            return "CheckPreviousAuthEvent"
        }
    }
}