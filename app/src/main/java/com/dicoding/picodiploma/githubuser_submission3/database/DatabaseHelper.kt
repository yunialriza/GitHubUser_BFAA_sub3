package com.dicoding.picodiploma.githubuser_submission3.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_AVATAR
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_COMPANY
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_FOLLOWERS
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_FOLLOWING
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_LOCATION
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_REPOSITORY
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_USER
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_USERNAME
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.TABLE_NAME


internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "Favorite User"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " ($COLUMNS_USERNAME TEXT PRIMARY KEY NOT NULL," +
                " $COLUMNS_USER TEXT NOT NULL," +
                " $COLUMNS_AVATAR TEXT NOT NULL," +
                " $COLUMNS_COMPANY TEXT NOT NULL," +
                " $COLUMNS_LOCATION TEXT NOT NULL," +
                " $COLUMNS_FOLLOWERS TEXT NOT NULL," +
                " $COLUMNS_FOLLOWING TEXT NOT NULL,"+
                " $COLUMNS_REPOSITORY TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}