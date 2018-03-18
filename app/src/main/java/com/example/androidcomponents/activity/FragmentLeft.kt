package com.example.androidcomponents.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidcomponents.R
import kotlinx.android.synthetic.main.fragment_left.*

class FragmentLeft : Fragment() {
    internal lateinit var com: Communicator // communication interface object

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            com = context as Communicator
        } catch (castException: ClassCastException) {
            Log.e("FragmentLeft", "BasicActivity didn't implement the Communicator interface")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_left, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_increment.setOnClickListener { com.increment() }
        btn_decrement.setOnClickListener { com.decrement() }
    }
}
