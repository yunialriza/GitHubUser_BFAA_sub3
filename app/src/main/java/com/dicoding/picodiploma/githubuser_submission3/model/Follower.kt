package com.dicoding.picodiploma.githubuser_submission3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Follower(
    var id: String? = "",
    var type: String? = "",
    var avatar: String? = "",
    var username: String? = ""
) : Parcelable