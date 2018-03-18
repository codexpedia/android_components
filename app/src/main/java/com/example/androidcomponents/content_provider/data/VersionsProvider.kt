package com.example.androidcomponents.content_provider.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.util.Log


class VersionsProvider : ContentProvider() {
    private var mOpenHelper: VersionsDBHelper? = null

    override fun onCreate(): Boolean {
        mOpenHelper = VersionsDBHelper(context)
        return true
    }

    override fun getType(uri: Uri): String? {
        val match = sUriMatcher.match(uri)

        when (match) {
            VERSION -> {
                return VersionsContract.VersionEntry.CONTENT_DIR_TYPE
            }
            VERSION_WITH_ID -> {
                return VersionsContract.VersionEntry.CONTENT_ITEM_TYPE
            }
            else -> {
                throw UnsupportedOperationException("Unknown uri: " + uri)
            }
        }
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val retCursor: Cursor
        when (sUriMatcher.match(uri)) {
        // All versions selected
            VERSION -> {
                retCursor = mOpenHelper!!.readableDatabase.query(
                        VersionsContract.VersionEntry.TABLE_VERSIONS,
                        projection,
                        selection,
                        selectionArgs, null, null,
                        sortOrder)
                return retCursor
            }
        // Individual version based on Id selected
            VERSION_WITH_ID -> {
                retCursor = mOpenHelper!!.readableDatabase.query(
                        VersionsContract.VersionEntry.TABLE_VERSIONS,
                        projection,
                        VersionsContract.VersionEntry._ID + " = ?",
                        arrayOf(ContentUris.parseId(uri).toString()), null, null,
                        sortOrder)
                return retCursor
            }
            else -> {
                // By default, we assume a bad URI
                throw UnsupportedOperationException("Unknown uri: " + uri)
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = mOpenHelper!!.writableDatabase
        val returnUri: Uri
        when (sUriMatcher.match(uri)) {
            VERSION -> {
                val _id = db.insert(VersionsContract.VersionEntry.TABLE_VERSIONS, null, values)
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = VersionsContract.VersionEntry.buildVersionsUri(_id)
                } else {
                    throw android.database.SQLException("Failed to insert row into: " + uri)
                }
            }

            else -> {
                throw UnsupportedOperationException("Unknown uri: " + uri)

            }
        }
        context!!.contentResolver.notifyChange(uri, null)
        return returnUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = mOpenHelper!!.writableDatabase
        val match = sUriMatcher.match(uri)
        val numDeleted: Int
        when (match) {
            VERSION -> {
                numDeleted = db.delete(VersionsContract.VersionEntry.TABLE_VERSIONS, selection, selectionArgs)
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        VersionsContract.VersionEntry.TABLE_VERSIONS + "'")
            }
            VERSION_WITH_ID -> {
                numDeleted = db.delete(VersionsContract.VersionEntry.TABLE_VERSIONS,
                        VersionsContract.VersionEntry._ID + " = ?",
                        arrayOf(ContentUris.parseId(uri).toString()))
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        VersionsContract.VersionEntry.TABLE_VERSIONS + "'")
            }
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }

        return numDeleted
    }

    override fun bulkInsert(uri: Uri, values: Array<ContentValues>): Int {
        val db = mOpenHelper!!.writableDatabase
        val match = sUriMatcher.match(uri)
        when (match) {
            VERSION -> {
                // allows for multiple transactions
                db.beginTransaction()

                // keep track of successful inserts
                var numInserted = 0
                try {
                    for (value in values) {
                        if (value == null) {
                            throw IllegalArgumentException("Cannot have null content values")
                        }
                        var _id: Long = -1
                        try {
                            _id = db.insertOrThrow(VersionsContract.VersionEntry.TABLE_VERSIONS, null, value)
                        } catch (e: SQLiteConstraintException) {
                            Log.w(LOG_TAG, e.toString())
                        }

                        if (_id != -1L) {
                            numInserted++
                        }
                    }

                    if (numInserted > 0) {
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful()
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction()
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there was a change
                    context!!.contentResolver.notifyChange(uri, null)
                }
                return numInserted
            }
            else -> return super.bulkInsert(uri, values)
        }
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val db = mOpenHelper!!.writableDatabase
        var numUpdated = 0

        if (contentValues == null) {
            throw IllegalArgumentException("Cannot have null content values")
        }

        when (sUriMatcher.match(uri)) {
            VERSION -> {
                numUpdated = db.update(VersionsContract.VersionEntry.TABLE_VERSIONS,
                        contentValues,
                        selection,
                        selectionArgs)
            }
            VERSION_WITH_ID -> {
                numUpdated = db.update(VersionsContract.VersionEntry.TABLE_VERSIONS,
                        contentValues,
                        VersionsContract.VersionEntry._ID + " = ?",
                        arrayOf(ContentUris.parseId(uri).toString()))
            }
            else -> {
                throw UnsupportedOperationException("Unknown uri: " + uri)
            }
        }

        if (numUpdated > 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }

        return numUpdated
    }

    companion object {
        private val LOG_TAG = VersionsProvider::class.java.simpleName
        private val sUriMatcher = buildUriMatcher()

        // Codes for the UriMatcher
        private val VERSION = 100
        private val VERSION_WITH_ID = 200

        private fun buildUriMatcher(): UriMatcher {
            // Build a UriMatcher by adding a specific code to return based on a match
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = VersionsContract.CONTENT_AUTHORITY

            // add a code for each type of URI you want
            matcher.addURI(authority, VersionsContract.VersionEntry.TABLE_VERSIONS, VERSION)
            matcher.addURI(authority, VersionsContract.VersionEntry.TABLE_VERSIONS + "/#", VERSION_WITH_ID)

            return matcher
        }
    }

}
