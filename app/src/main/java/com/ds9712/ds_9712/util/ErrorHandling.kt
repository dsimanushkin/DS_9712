package com.ds9712.ds_9712.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.ds9712.ds_9712.R

class ErrorHandling {
    companion object {
        const val NETWORK_ERROR = "Network Error"
        const val NETWORK_ERROR_TIMEOUT = "Network Timeout"
        const val CACHE_ERROR_TIMEOUT = "Cache Timeout"
        const val UNKNOWN_ERROR = "Unknown Error"
        const val INVALID_STATE_EVENT = "Invalid state event"

        fun handleErrors(statusCode: Int?, application: Application): String {
            // 1001 - Full name field required.
            // 1002 - Full name too short.
            // 1003 - Full Name too long.

            // 1011 - Username field required.
            // 1012 - Account with this username already exists.
            // 1013 - Username too short.
            // 1014 - Username too long.
            // 1015 - Username is available.

            // 1021 - Email field required.
            // 1022 - Account with this email already exists.
            // 1023 - Invalid email format.
            // 1024 - Email is available.

            // 1031 - Password field required.
            // 1032 - Password is too short.
            // 1033 - Password is valid.

            // 1041 - Date Of Birth field required.
            // 1042 - You should be at least 14 years old.
            // 1043 - Date of birth is valid.

            // 2001 - Successfully registered new user.
            // 2002 - Successfully retrieved account properties.
            // 2003 - Agreement accepted successfully and verification code sent.
            // 2004 - Agreement accepted successfully and verification code not sent.
            // 2005 - Successfully retrieved account verification fields.
            // 2006 - Account deleted successfully.
            // 2007 - Error deleting account.
            // 2008 - Verification code has been sent.
            // 2009 - Unable to send verification email due to reached daily limit.
            // 2010 - Account has been already verified.
            // 2011 - Account with this credentials not found.
            // 2012 - User successfully signed in.
            // 2013 - Email updated successfully.
            // 2014 - Password is incorrect.
            // 2015 - Email could be changed only once a day.
            // 2016 - Agreement has already been accepted.
            // 2017 - Email changed successfully and new verification code has been sent.
            // 2018 - Email should be verified before resetting password.
            // 2019 - Reset password email has been sent.
            // 2020 - Unable to send reset password email due to reached daily limit.
            // 2021 - Verification code is valid.
            // 2022 - Verification code is invalid.
            // 2023 - Verification code required.
            // 2024 - Email has been already verified.

            // 5001 - Invalid authorization token.
            // 5002 - Record not found.

            return when(statusCode) {
//                1001 -> application.getString(R.string.full_name_is_required)
//                1002 -> application.getString(R.string.full_name_field_is_too_short)
//                1003 -> application.getString(R.string.full_name_field_is_too_long)
//
//                1011 -> application.getString(R.string.username_is_required)
//                1012 -> application.getString(R.string.this_username_already_in_use)
//                1013 -> application.getString(R.string.username_field_is_too_short)
//                1014 -> application.getString(R.string.username_field_is_too_long)
//                1015 -> application.getString(R.string.username_is_available)
//
//                1021 -> application.getString(R.string.email_is_required)
//                1022 -> application.getString(R.string.this_email_already_in_use)
//                1023 -> application.getString(R.string.invalid_email_format)
//                1024 -> application.getString(R.string.email_is_available)
//
//                1031 -> application.getString(R.string.password_field_required)
//                1032 -> application.getString(R.string.password_is_too_short)
//                1033 -> application.getString(R.string.password_is_valid)
//
//                1041 -> application.getString(R.string.date_of_birth_field_required)
//                1042 -> application.getString(R.string.you_should_be_at_least_14_years_old)
//                1043 -> application.getString(R.string.date_of_birth_is_valid)
//
//                1044 -> application.getString(R.string.paypal_account_field_required)
//
//                1051 -> application.getString(R.string.paypal_account_field_required)
//                1052 -> application.getString(R.string.paypal_account_field_is_valid)
//
//                2001 -> application.getString(R.string.successfully_registered_new_user)
//                2002 -> application.getString(R.string.successfully_retrieved_account_properties)
//                2003 -> application.getString(R.string.agreement_accepted_successfully_and_verification_code_sent)
//                2004 -> application.getString(R.string.agreement_accepted_successfully_and_verification_code_not_sent)
//                2005 -> application.getString(R.string.successfully_retrieved_account_verification_fields)
//                2006 -> application.getString(R.string.account_deleted_successfully)
//                2007 -> application.getString(R.string.error_deleting_account)
//                2008 -> application.getString(R.string.verification_code_has_been_sent)
//                2009 -> application.getString(R.string.unable_to_send_verification_email_due_to_reached_daily_limit)
//                2010 -> application.getString(R.string.account_has_been_already_verified)
//                2011 -> application.getString(R.string.account_with_this_credentials_not_found)
//                2012 -> application.getString(R.string.user_successfully_signed_in)
//                2013 -> application.getString(R.string.email_updated_successfully)
//                2014 -> application.getString(R.string.password_is_incorrect)
//                2015 -> application.getString(R.string.email_could_be_changed_only_once_a_day)
//                2016 -> application.getString(R.string.agreement_has_already_been_accepted)
//                2017 -> application.getString(R.string.email_changed_successfully_and_new_verification_code_has_been_sent)
//                2018 -> application.getString(R.string.email_should_be_verified_before_resetting_password)
//                2019 -> application.getString(R.string.reset_password_email_has_been_sent)
//                2020 -> application.getString(R.string.unable_to_send_reset_password_email_due_to_reached_daily_limit)
//                2021 -> application.getString(R.string.verification_code_is_valid)
//                2022 -> application.getString(R.string.verification_code_is_invalid)
//                2023 -> application.getString(R.string.verification_code_required)
//                2024 -> application.getString(R.string.email_has_been_already_verified)
//
//                5001 -> application.getString(R.string.invalid_authorization_token)
//                5002 -> application.getString(R.string.record_not_found)
//
//                9001 -> application.getString(R.string.unknown_error)
//                9002 -> application.getString(R.string.generic_success)
//                9003 -> application.getString(R.string.generic_error)
//                9004 -> application.getString(R.string.error_save_auth_token)
//                9005 -> application.getString(R.string.error_save_account_properties)
//                9006 -> application.getString(R.string.response_check_previous_auth_user_done)
//
//                else -> application.getString(R.string.unknown_error)
                else -> ""
            }
        }

        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }
                    }
                }
            }
            return result
        }
    }
}