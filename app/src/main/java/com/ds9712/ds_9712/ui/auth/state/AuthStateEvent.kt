package com.ds9712.ds_9712.ui.auth.state

import com.ds9712.ds_9712.util.StateEvent
import java.util.*

sealed class AuthStateEvent: StateEvent {

    object AccountStatusEvent : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error checking account status."
        }

        override fun toString(): String {
            return "AccountStatusEvent"
        }
    }

    object CheckPreviousAuthEvent : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error checking for previously authenticated user."
        }

        override fun toString(): String {
            return "CheckPreviousAuthEvent"
        }
    }

    data class LoginEvent(
        val username: String,
        val password: String
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Failed to login user."
        }

        override fun toString(): String {
            return "LoginEvent"
        }
    }

    data class ValidateSignUpStepOneFieldsEvent(
        val fullName: String,
        val username: String
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Failed to validate fields for sign up step one."
        }

        override fun toString(): String {
            return "ValidateSignUpStepOneFieldsEvent"
        }
    }

    data class ValidateSignUpStepTwoFieldsEvent(
        val email: String
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Failed to validate fields for sign up step two."
        }

        override fun toString(): String {
            return "ValidateSignUpStepTwoFieldsEvent"
        }
    }

    data class ValidateSignUpStepThreeEvent(
        val password: String,
        val confirmPassword: String
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Failed to validate fields for sign up step three."
        }

        override fun toString(): String {
            return "ValidateSignUpStepThreeEvent"
        }
    }

    data class SignUpEvent(
        val fullName: String,
        val username: String,
        val email: String,
        val password: String,
        val dateOfBirth: Calendar
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Failed to sign up user."
        }

        override fun toString(): String {
            return "SignUpEvent"
        }
    }

    object AcceptAgreementEvent: AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error accepting agreement."
        }

        override fun toString(): String {
            return "AcceptAgreementEvent"
        }
    }

    object DeclineAgreementEvent: AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error declining agreement."
        }

        override fun toString(): String {
            return "DeclineAgreementEvent"
        }
    }

    data class VerifyEmailEvent(
        val verificationCode: String
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error verifying email."
        }

        override fun toString(): String {
            return "VerifyEmailEvent"
        }
    }

    object ResendVerificationCodeEvent: AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error resending verification code."
        }

        override fun toString(): String {
            return "ResendVerificationCodeEvent"
        }
    }

    data class ChangeEmailAddressEvent(
        val email: String,
        val password: String
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error changing email address."
        }

        override fun toString(): String {
            return "ChangeEmailAddressEvent"
        }
    }
}