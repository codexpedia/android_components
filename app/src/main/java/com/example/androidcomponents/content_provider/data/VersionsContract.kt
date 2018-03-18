package com.example.androidcomponents.content_provider.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

object VersionsContract {

    val CONTENT_AUTHORITY = "com.example.androidcomponents"

    val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)


    class VersionEntry : BaseColumns {
        companion object {
            // table name
            val TABLE_VERSIONS = "versions"
            // columns
            val _ID = "_id"
            val COLUMN_DESCRIPTION = "description"
            val COLUMN_VERSION_NAME = "version_name"
            val COLUMN_VERSION_CODE = "version_code"
            val COLUMN_API_LEVEL = "api_level"

            // content uri
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_VERSIONS).build()

            // cursor of base type directory for multiple entries
            val CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_VERSIONS

            // cursor of base type item for single entry
            val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_VERSIONS

            // insertion URIs
            fun buildVersionsUri(id: Long): Uri {
                return ContentUris.withAppendedId(CONTENT_URI, id)
            }
        }
    }
}
