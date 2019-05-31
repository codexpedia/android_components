package com.example.androidcomponents.content_provider.views

import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.androidcomponents.R
import com.example.androidcomponents.content_provider.data.VersionsContract


class FragmentVersionDetail : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var mDetailCursor: Cursor? = null
    private var mPosition: Int = 0
    private var tvVersionName: TextView? = null
    private var tvVersionCode: TextView? = null
    private var tvApiLevel: TextView? = null
    private var tvUri: TextView? = null
    private var tvVersionDescription: TextView? = null
    private lateinit var mUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_version_detail, container, false)
        tvVersionName = rootView.findViewById<View>(R.id.tv_version_name) as TextView
        tvVersionCode = rootView.findViewById<View>(R.id.tv_version_code) as TextView
        tvApiLevel = rootView.findViewById<View>(R.id.tv_api_level) as TextView
        tvUri = rootView.findViewById<View>(R.id.tv_uri) as TextView
        tvVersionDescription = rootView.findViewById<View>(R.id.tv_version_description) as TextView
        val args = this.arguments
        loaderManager.initLoader(CURSOR_LOADER_ID, args, this@FragmentVersionDetail)

        return rootView
    }


    override fun onDetach() {
        super.onDetach()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        if (args != null) {
            selection = VersionsContract.VersionEntry._ID
            selectionArgs = arrayOf(mPosition.toString())
        }
        return CursorLoader(activity!!,
                mUri, null,
                selection,
                selectionArgs, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mDetailCursor = data
        mDetailCursor!!.moveToFirst()
        DatabaseUtils.dumpCursor(data)
        tvVersionName!!.text = mDetailCursor!!.getString(1)
        tvVersionCode!!.text = mDetailCursor!!.getString(2)
        tvApiLevel!!.text = mDetailCursor!!.getString(3)
        tvVersionDescription!!.text = mDetailCursor!!.getString(4)

        tvUri!!.text = mUri!!.toString()
    }

    // reset CursorAdapter on Loader Reset
    override fun onLoaderReset(loader: Loader<Cursor>) {
        mDetailCursor = null
    }

    companion object {
        private val CURSOR_LOADER_ID = 0


        fun newInstance(position: Int, uri: Uri): FragmentVersionDetail {
            val fragment = FragmentVersionDetail()
            val args = Bundle()
            fragment.mPosition = position
            fragment.mUri = uri
            args.putInt("id", position)
            fragment.arguments = args
            return fragment
        }
    }

}
