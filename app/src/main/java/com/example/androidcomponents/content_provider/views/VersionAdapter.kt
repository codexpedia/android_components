package com.example.androidcomponents.content_provider.views

import android.content.Context
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.androidcomponents.R
import com.example.androidcomponents.content_provider.data.VersionsContract

class VersionAdapter(context: Context, c: Cursor?, flags: Int) : CursorAdapter(context, c, flags) {

    init {
        mContext = context
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        val layoutId = R.layout.version_item

        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        val viewHolder = ViewHolder(view)
        view.tag = viewHolder

        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val viewHolder = view.tag as ViewHolder

        val versionNameIndex = cursor.getColumnIndex(VersionsContract.VersionEntry.COLUMN_VERSION_NAME)
        val versionCodeIndex = cursor.getColumnIndex(VersionsContract.VersionEntry.COLUMN_VERSION_CODE)
        val apiLevelIndex = cursor.getColumnIndex(VersionsContract.VersionEntry.COLUMN_API_LEVEL)

        viewHolder.tvVersionName.text = cursor.getString(versionNameIndex)
        viewHolder.tvVersionCode.text = cursor.getString(versionCodeIndex)
        viewHolder.tvApiLevel.text = cursor.getString(apiLevelIndex)
    }


    class ViewHolder(view: View) {
        val tvVersionName: TextView
        val tvVersionCode: TextView
        val tvApiLevel: TextView

        init {
            tvVersionName = view.findViewById<View>(R.id.tv_version_name) as TextView
            tvVersionCode = view.findViewById<View>(R.id.tv_version_code) as TextView
            tvApiLevel = view.findViewById<View>(R.id.tv_api_level) as TextView
        }
    }

}
