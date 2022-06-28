package com.tavanhieu.noteapp.fragNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tavanhieu.noteapp.R

class FragmentDefaultView: Fragment() {
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        return inflater.inflate(R.layout.fragment_default_view, container, false)
    }
}