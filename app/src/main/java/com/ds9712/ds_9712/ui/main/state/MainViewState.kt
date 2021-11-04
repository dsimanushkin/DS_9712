package com.ds9712.ds_9712.ui.main.state

import android.os.Parcelable
import com.ds9712.ds_9712.models.CurrentQuestion
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainViewState(
    var currentQuestionFields: CurrentQuestionFields = CurrentQuestionFields()
): Parcelable {
    @Parcelize
    data class CurrentQuestionFields(
        var currentQuestion: CurrentQuestion? = null
    ): Parcelable
}