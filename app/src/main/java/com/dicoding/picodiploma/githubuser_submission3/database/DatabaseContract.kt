package com.dicoding.picodiploma.githubuser_submission3.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.dicoding.picodiploma.githubuser_submission3"
    const val SCHEME = "content"

    class FavColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "FavoriteUser"
            const val COLUMNS_USER = "user"
            const val COLUMNS_USERNAME = "username"
            const val COLUMNS_AVATAR = "avatar"
            const val COLUMNS_COMPANY = "company"
            const val COLUMNS_LOCATION = "location"
            const val COLUMNS_REPOSITORY = "repository"
            const val COLUMNS_FOLLOWERS = "followers"
            const val COLUMNS_FOLLOWING = "following"


//            Uri Content
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}