package com.example.androidcomponents.content_provider.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class VersionsDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                VersionsContract.VersionEntry.TABLE_VERSIONS + "(" + VersionsContract.VersionEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VersionsContract.VersionEntry.COLUMN_VERSION_NAME + " TEXT NOT NULL, " +
                VersionsContract.VersionEntry.COLUMN_VERSION_CODE + " TEXT NOT NULL, " +
                VersionsContract.VersionEntry.COLUMN_API_LEVEL + " TEXT NOT NULL, " +
                VersionsContract.VersionEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL);"

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE)
    }

    // Upgrade database when version is changed.
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED")

        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VersionsContract.VersionEntry.TABLE_VERSIONS)
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                VersionsContract.VersionEntry.TABLE_VERSIONS + "'")

        // re-create database
        onCreate(sqLiteDatabase)
    }

    companion object {
        val LOG_TAG = VersionsDBHelper::class.java.simpleName

        private val DATABASE_NAME = "android_versions.db"
        private val DATABASE_VERSION = 2
    }
}
