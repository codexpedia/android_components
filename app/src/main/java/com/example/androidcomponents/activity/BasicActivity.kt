package com.example.androidcomponents.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.example.androidcomponents.R

class BasicActivity : AppCompatActivity(), Communicator {

    private var fragmentLeft: FragmentLeft? = null
    private var fragmentRight: FragmentRight? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)

        fragmentLeft = FragmentLeft()
        val fragTag = "fragment_left"
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_fragment_left, fragmentLeft!!, fragTag)
                .addToBackStack(null)
                .commit()

        fragmentRight = FragmentRight()
        val fragTag2 = "fragment_right"
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_fragment_right, fragmentRight!!, fragTag2)
                .addToBackStack(null)
                .commit()

    }

    override fun increment() {
        fragmentRight!!.updateCounter(counter++)
    }

    override fun decrement() {
        fragmentRight!!.updateCounter(counter--)
    }

    companion object {
        private var counter = 0
    }
}
