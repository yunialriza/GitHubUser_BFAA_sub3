package com.dicoding.picodiploma.consumerapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Following (
    var id: String? = "",
    var type: String? = "",
    var avatar: String? = "",
    var username: String? = ""
        ): Parcelable