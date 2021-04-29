package com.dicoding.picodiploma.githubuser_submission3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.AUTHORITY
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.githubuser_submission3.database.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    companion object{
        private const val FAVORITE = 1
        private const val COLUMNS_USERNAME = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init {
            // content:// com.dicoding.picodiploma.githubuser_submission3/favorite
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)

            // content:// com.dicoding.picodiploma.githubuser_submission3/favorite/username
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", COLUMNS_USERNAME)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(sUriMatcher.match(uri)){
            FAVORITE -> favoriteHelper.queryAll()
            COLUMNS_USERNAME -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
       return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = favoriteHelper.deleteById(uri.lastPathSegment.toString())
        return if (deleted == 1) {
            context?.contentResolver?.notifyChange(CONTENT_URI, null)
            deleted
        } else {
            0
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val updated: Int = when (COLUMNS_USERNAME) {
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(),
                values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }


}