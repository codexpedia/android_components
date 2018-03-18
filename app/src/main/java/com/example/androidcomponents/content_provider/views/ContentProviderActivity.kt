package com.example.androidcomponents.content_provider.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.example.androidcomponents.R

class ContentProviderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                FragmentContentProvider()).commit()

    }
}
