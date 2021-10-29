package com.ds9712.ds_9712.ui.auth.state

import android.app.Application
import android.os.Parcelable
import com.ds9712.ds_9712.R
import com.ds9712.ds_9712.models.AccountStatus
import com.ds9712.ds_9712.models.AuthToken
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class AuthViewState(
    var authToken: AuthToken? = null,
    var accountStatus: AccountStatus? = null,
    var loginFields: LoginFields? = null,
    var signUpFields: SignUpFields? = null,
    var forgotPasswordFields: ForgotPasswordFields? = null,
    var signUpStepOneFields: SignUpStepOneFields? = null,
    var signUpStepTwoFields: SignUpStepTwoFields? = null,
    var signUpStepThreeFields: SignUpStepThreeFields? = null,
    var signUpStepFourFields: SignUpStepFourFields? = null
): Parcelable

@Parcelize
data class LoginFields(
    var loginUsername: String? = null,
    var loginPassword: String? = null
): Parcelable {
    class LoginError {
        companion object {
            fun mustFillUsernameField(application: Application): String {
                return application.getString(R.string.log_in_fragment_error_empty_username_field)
            }
            fun mustFillPasswordField(application: Application): String {
                return application.getString(R.string.log_in_fragment_error_empty_password_field)
            }
            fun none(application: Application): String {
                return application.getString(R.string.error_none)
            }
        }
    }

    fun isValidForSubmission(application: Application): String {
        return when {
            loginUsername.isNullOrEmpty() -> LoginError.mustFillUsernameField(application)
            loginPassword.isNullOrEmpty() -> LoginError.mustFillPasswordField(application)
            else -> LoginError.none(application)
        }
    }
}

@Parcelize
data class ForgotPasswordFields(
    var forgotPasswordUsername: String? = null
): Parcelable {
    class ForgotPasswordError {
        companion object {
            fun mustFillUsernameField(application: Application): String {
                return application.getString(R.string.forgot_password_fragment_error_empty_username_field)
            }
            fun none(application: Application): String {
                return application.getString(R.string.error_none)
            }
        }
    }

    fun isValidForSubmission(application: Application): String {
        return when {
            forgotPasswordUsername.isNullOrEmpty() -> ForgotPasswordError.mustFillUsernameField(application)
            else -> ForgotPasswordError.none(application)
        }
    }
}

@Parcelize
data class SignUpFields(
    var fullName: String? = null,
    var username: String? = null,
    var email: String? = null,
    var password: String? = null,
    var dateOfBirth: Calendar? = null,
    var payPalUsername: String? = null
): Parcelable {
    class SignUpError {
        companion object {
            fun mustFillPayPalUsernameField(application: Application): String {
                return application.getString(R.string.sign_up_step_five_fragment_error_empty_email_field)
            }
            fun none(application: Application): String {
                return application.getString(R.string.error_none)
            }
        }
    }

    fun isValidForSubmission(application: Application): String {
        return if (payPalUsername.isNullOrEmpty()) {
            SignUpError.mustFillPayPalUsernameField(application)
        } else {
            SignUpError.none(application)
        }
    }
}

@Parcelize
data class SignUpStepOneFields(
    var signUpFullName: String? = null,
    var signUpUsername: String? = null
): Parcelable {
    class SignUpStepOneError {
        companion object {
            fun mustFillFullNameField(application: Application): String {
                return application.getString(R.string.sign_up_step_one_fragment_error_empty_full_name_field)
            }
            fun mustFillUsernameField(application: Application): String {
                return application.getString(R.string.sign_up_step_one_fragment_error_empty_username_field)
            }
            fun none(application: Application): String {
                return application.getString(R.string.error_none)
            }
        }
    }

    fun isValidForSubmission(application: Application): String {
        return when {
            signUpFullName.isNullOrEmpty() -> SignUpStepOneError.mustFillFullNameField(application)
            signUpUsername.isNullOrEmpty() -> SignUpStepOneError.mustFillUsernameField(application)
            else -> SignUpStepOneError.none(application)
        }
    }
}

@Parcelize
data class SignUpStepTwoFields(
    var signUpEmail: String? = null
): Parcelable {
    class SignUpStepTwoError {
        companion object {
            fun mustFillEmailField(application: Application): String {
                return application.getString(R.string.sign_up_step_two_fragment_error_empty_email_field)
            }
            fun none(application: Application): String {
                return application.getString(R.string.error_none)
            }
        }
    }

    fun isValidForSubmission(application: Application): String {
        return when {
            signUpEmail.isNullOrEmpty() -> SignUpStepTwoError.mustFillEmailField(application)
            else -> SignUpStepTwoError.none(application)
        }
    }
}

@Parcelize
data class SignUpStepThreeFields(
    var signUpPassword: String? = null,
    var signUpConfirmPassword: String? = null
): Parcelable {
    class SignUpStepThreeError {
        companion object {
            fun mustFillPasswordField(application: Application): String {
                return application.getString(R.string.sign_up_step_three_fragment_error_empty_password_field)
            }
            fun passwordsDoNotMatch(application: Application): String {
                return application.getString(R.string.sign_up_step_three_fragment_error_passwords_do_not_match)
            }
            fun none(application: Application): String {
                return application.getString(R.string.error_none)
            }
        }
    }

    fun isValidForSubmission(application: Application): String {
        return if (signUpPassword.isNullOrEmpty() || signUpConfirmPassword.isNullOrEmpty()) {
            SignUpStepThreeError.mustFillPasswordField(application)
        } else if (signUpPassword != signUpConfirmPassword) {
            SignUpStepThreeError.passwordsDoNotMatch(application)
        } else {
            SignUpStepThreeError.none(application)
        }
    }
}

@Parcelize
data class SignUpStepFourFields(
    var dateOfBirth: Calendar? = null
): Parcelable {
    class SignUpStepFourError {
        companion object {
            fun mustBe18OrOlder(application: Application): String {
                return application.getString(R.string.you_should_be_at_least_18_years_old)
            }
            fun none(application: Application): String {
                return application.getString(R.string.error_none)
            }
        }
    }

    fun isValidForSubmission(application: Application): String {
        val currentDate: Calendar = Calendar.getInstance()
        return if (currentDate.get(Calendar.YEAR) - dateOfBirth?.get(Calendar.YEAR)!! < 18) {
            SignUpStepFourError.mustBe18OrOlder(application)
        } else {
            SignUpStepFourError.none(application)
        }
    }
}