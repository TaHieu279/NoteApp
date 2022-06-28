package com.tavanhieu.noteapp.fragNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.noteapp.madapter.AdapterRecycleNote
import com.tavanhieu.noteapp.ArrNote
import com.tavanhieu.noteapp.R

class FragmentListView: Fragment() {
    private lateinit var rcvFragNote: RecyclerView
    private lateinit var mView: View

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        mView = inflater.inflate(R.layout.fragment_recycle_view, container, false)
        rcvFragNote = mView.findViewById(R.id.recycleViewFragNote)

        val mAdapter = AdapterRecycleNote(requireContext())
        mAdapter.setData(ArrNote.arr)
        rcvFragNote.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rcvFragNote.adapter = mAdapter
        return mView
    }
}