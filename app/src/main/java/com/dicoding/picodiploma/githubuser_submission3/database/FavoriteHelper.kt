package com.dicoding.picodiploma.githubuser_submission3.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_USERNAME
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.TABLE_NAME
import java.sql.SQLException

class FavoriteHelper(context: Context) {


    companion object{
    private const val DATABASE_TABLE = TABLE_NAME
    private var INSTANCE: FavoriteHelper? = null
    fun getInstance(context: Context): FavoriteHelper =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteHelper(context.applicationContext)
        }
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }
    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }
    fun close() {
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMNS_USERNAME ASC")
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$COLUMNS_USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }
        //methode insert
    fun insert(values: ContentValues?):Long{
        return database.insert(DATABASE_TABLE, null, values)
    }

    //methode delete
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$COLUMNS_USERNAME = '$id'", null)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$COLUMNS_USERNAME= ?", arrayOf(id))
    }
}