package com.example.androidcomponents.content_provider.views

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.example.androidcomponents.R
import com.example.androidcomponents.content_provider.data.Version
import com.example.androidcomponents.content_provider.data.VersionsContract


class FragmentContentProvider : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var mVersionAdapter: VersionAdapter? = null
    private var lvVersions: ListView? = null
    internal var versions = arrayOf(Version("Cupcake", "1.5", "3", "The first release of Android"), Version("Donut", "1.6", "4", "The world's information is at your fingertips – search the web, get driving directions... or just watch cat videos."), Version("Eclair", "2.0-2.1", "5-7", "Make your home screen just how you want it. Arrange apps and widgets across multiple screens and in folders. Stunning live wallpapers " + "respond to your touch."), Version("Froyo", "2.2-2.2.3", "8", "Voice Typing lets you input text, and Voice Actions let you control your phone, just by speaking."), Version("GingerBread", "2.3-2.3.7", "9-10", "New sensors make Android great for gaming - so you can touch, tap, tilt, and play away."), Version("Honeycomb", "3.0-3.2.6", "11-13", "Optimized for tablets, this release opens up new horizons wherever you are."), Version("Ice Cream Sandwich", "4.0-4.0.4", "14-15", "Android comes of age with a new, refined design. Simple, beautiful and beyond smart."), Version("Jelly Bean", "4.1-4.3.1", "16-18", "Android is fast and smooth with buttery graphics. With Google Now, you get just the right information at the right time."), Version("KitKat", "4.4-4.4.4", "19-20", "Smart, simple, and truly yours. A more polished design, improved performance, and new features."), Version("Lollipop", "5.0-5.1.1", "21-22", "A sweet new take on Android. Get the smarts of Android on screens big and small – with the right information at the right moment."), Version("Marshmallow", "6.0–6.0.1", "23", "Now on Tap anticipates what you need in the moment. With a simple tap, you can get cards with useful information and apps that feed your need to know."), Version("Nougat", "7.0–7.1.2", "24–25", "With more ways to make Android your own, Android Nougat is our sweetest release yet."))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val c = activity.contentResolver.query(VersionsContract.VersionEntry.CONTENT_URI,
                arrayOf(VersionsContract.VersionEntry._ID), null, null, null)
        if (c!!.count == 0) {
            insertData()
        }
        // initialize loader
        loaderManager.initLoader(CURSOR_LOADER_ID, Bundle(), this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // inflate fragment_main layout
        val rootView = inflater!!.inflate(R.layout.fragment_content_provider, container, false)

        // initialize our VersionAdapter
        mVersionAdapter = VersionAdapter(activity, null, 0)
        lvVersions = rootView.findViewById<View>(R.id.lv_versions) as ListView
        lvVersions!!.adapter = mVersionAdapter

        lvVersions!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // increment the position to match Database Ids indexed starting at 1
            val uriId = position + 1
            val uri = ContentUris.withAppendedId(VersionsContract.VersionEntry.CONTENT_URI, uriId.toLong())

            val detailFragment = FragmentVersionDetail.newInstance(uriId, uri)
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null).commit()
        }


        return rootView
    }

    // insert data into database
    fun insertData() {
        val versionValuesArr = arrayOfNulls<ContentValues>(versions.size)
        for (i in versions.indices) {
            versionValuesArr[i] = ContentValues()
            versionValuesArr[i]?.put(VersionsContract.VersionEntry.COLUMN_VERSION_NAME, versions[i].versionName)
            versionValuesArr[i]?.put(VersionsContract.VersionEntry.COLUMN_VERSION_CODE, versions[i].versionCode)
            versionValuesArr[i]?.put(VersionsContract.VersionEntry.COLUMN_API_LEVEL, versions[i].apiLevel)
            versionValuesArr[i]?.put(VersionsContract.VersionEntry.COLUMN_DESCRIPTION, versions[i].description)
        }

        // bulkInsert our ContentValues array
        activity.contentResolver.bulkInsert(VersionsContract.VersionEntry.CONTENT_URI, versionValuesArr)
    }

    // Attach loader to our versions database query, run when loader is initialized
    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> {
        return CursorLoader(activity,
                VersionsContract.VersionEntry.CONTENT_URI, null, null, null, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mVersionAdapter!!.swapCursor(data)
    }

    // reset CursorAdapter on Loader Reset
    override fun onLoaderReset(loader: Loader<Cursor>) {
        mVersionAdapter!!.swapCursor(null)
    }

    companion object {

        private val LOG_TAG = FragmentContentProvider::class.java.simpleName


        private val CURSOR_LOADER_ID = 0
    }
}
