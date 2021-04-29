package com.dicoding.picodiploma.consumerapp.database

import android.database.Cursor
import com.dicoding.picodiploma.consumerapp.model.FavoriteUser

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<FavoriteUser> {
        val favoriteList = ArrayList<FavoriteUser>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_USERNAME))
                val user =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_USER))
                val avatar =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_AVATAR))
                val company =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_COMPANY))
                val location =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_LOCATION))
                val follower =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_FOLLOWERS))
                val following =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_FOLLOWING))
                val repository =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_REPOSITORY))
                favoriteList.add(
                    FavoriteUser(
                        username, user, avatar, company, location, follower, following, repository
                    )
                )

            }
        }
        return favoriteList
    }

    fun mapCursorToObject(favoriteCursor: Cursor?): FavoriteUser {
        var userFavorite = FavoriteUser()
        favoriteCursor?.apply {
            moveToFirst()
            val username =
                getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_USERNAME))
            val user =
                getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_USER))
            val avatar =
                getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_AVATAR))
            val company =
                getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_COMPANY))
            val location =
                getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_LOCATION))
            val follower =
                getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_FOLLOWERS))
            val following =
                getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_FOLLOWING))
            val repository =
                getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COLUMNS_REPOSITORY))
            userFavorite = FavoriteUser(
                username, user, avatar, company, location, follower, following, repository
            )

        }
        return userFavorite
    }
}