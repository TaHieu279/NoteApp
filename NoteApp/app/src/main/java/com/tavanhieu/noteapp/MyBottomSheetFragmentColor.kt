package com.tavanhieu.noteapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyBottomSheetFragmentColor: BottomSheetDialogFragment() {
    private lateinit var btnCancel:  ImageView
    private lateinit var colorNote1: View
    private lateinit var colorNote2: View
    private lateinit var colorNote3: View
    private lateinit var colorNote4: View
    private lateinit var colorNote5: View
    private lateinit var colorNote6: View
    private lateinit var colorNote7: View
    private lateinit var colorNote8: View

    private lateinit var imgColorNote1: ImageView
    private lateinit var imgColorNote2: ImageView
    private lateinit var imgColorNote3: ImageView
    private lateinit var imgColorNote4: ImageView
    private lateinit var imgColorNote5: ImageView
    private lateinit var imgColorNote6: ImageView
    private lateinit var imgColorNote7: ImageView
    private lateinit var imgColorNote8: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialogFragment = super.onCreateDialog(savedInstanceState)
        val viewDialog = LayoutInflater.from(context).inflate(R.layout.fragment_color_note, null)
        bottomSheetDialogFragment.setContentView(viewDialog)

        anhXa(viewDialog)
        btnCancel.setOnClickListener { bottomSheetDialogFragment.dismiss() }
        mOnClick()

        return bottomSheetDialogFragment
    }

    private fun anhXa(view: View) {
        btnCancel  = view.findViewById(R.id.imgCancelChangeBackground)
        colorNote1 = view.findViewById(R.id.colorNote1)
        colorNote2 = view.findViewById(R.id.colorNote2)
        colorNote3 = view.findViewById(R.id.colorNote3)
        colorNote4 = view.findViewById(R.id.colorNote4)
        colorNote5 = view.findViewById(R.id.colorNote5)
        colorNote6 = view.findViewById(R.id.colorNote6)
        colorNote7 = view.findViewById(R.id.colorNote7)
        colorNote8 = view.findViewById(R.id.colorNote8)

        imgColorNote1 = view.findViewById(R.id.img_color_note_1)
        imgColorNote2 = view.findViewById(R.id.img_color_note_2)
        imgColorNote3 = view.findViewById(R.id.img_color_note_3)
        imgColorNote4 = view.findViewById(R.id.img_color_note_4)
        imgColorNote5 = view.findViewById(R.id.img_color_note_5)
        imgColorNote6 = view.findViewById(R.id.img_color_note_6)
        imgColorNote7 = view.findViewById(R.id.img_color_note_7)
        imgColorNote8 = view.findViewById(R.id.img_color_note_8)
    }

    private val myAction = "com.tavanhieu.ACTION_SEND_COLOR"
    private val key  = "colorBackground"

    private fun mSendBoastCard(value: String) {
        val intent = Intent(myAction)
        intent.putExtra(key, value)
        context?.sendBroadcast(intent)
    }

    private fun mOnClick() {
        colorNote1.setOnClickListener {
            imgColorNote1.setImageResource(R.drawable.ic_tick)
            imgColorNote2.setImageResource(0)
            imgColorNote3.setImageResource(0)
            imgColorNote4.setImageResource(0)
            imgColorNote5.setImageResource(0)
            imgColorNote6.setImageResource(0)
            imgColorNote7.setImageResource(0)
            imgColorNote8.setImageResource(0)
            mSendBoastCard("green")
        }
        colorNote2.setOnClickListener {
            imgColorNote1.setImageResource(0)
            imgColorNote2.setImageResource(R.drawable.ic_tick)
            imgColorNote3.setImageResource(0)
            imgColorNote4.setImageResource(0)
            imgColorNote5.setImageResource(0)
            imgColorNote6.setImageResource(0)
            imgColorNote7.setImageResource(0)
            imgColorNote8.setImageResource(0)
            mSendBoastCard("blue")
        }
        colorNote3.setOnClickListener {
            imgColorNote1.setImageResource(0)
            imgColorNote2.setImageResource(0)
            imgColorNote3.setImageResource(R.drawable.ic_tick)
            imgColorNote4.setImageResource(0)
            imgColorNote5.setImageResource(0)
            imgColorNote6.setImageResource(0)
            imgColorNote7.setImageResource(0)
            imgColorNote8.setImageResource(0)
            mSendBoastCard("yellow")
        }
        colorNote4.setOnClickListener {
            imgColorNote1.setImageResource(0)
            imgColorNote2.setImageResource(0)
            imgColorNote3.setImageResource(0)
            imgColorNote4.setImageResource(R.drawable.ic_tick)
            imgColorNote5.setImageResource(0)
            imgColorNote6.setImageResource(0)
            imgColorNote7.setImageResource(0)
            imgColorNote8.setImageResource(0)
            mSendBoastCard("blue_white")
        }
        colorNote5.setOnClickListener {
            imgColorNote1.setImageResource(0)
            imgColorNote2.setImageResource(0)
            imgColorNote3.setImageResource(0)
            imgColorNote4.setImageResource(0)
            imgColorNote5.setImageResource(R.drawable.ic_tick)
            imgColorNote6.setImageResource(0)
            imgColorNote7.setImageResource(0)
            imgColorNote8.setImageResource(0)
            mSendBoastCard("purple")
        }
        colorNote6.setOnClickListener {
            imgColorNote1.setImageResource(0)
            imgColorNote2.setImageResource(0)
            imgColorNote3.setImageResource(0)
            imgColorNote4.setImageResource(0)
            imgColorNote5.setImageResource(0)
            imgColorNote6.setImageResource(R.drawable.ic_tick)
            imgColorNote7.setImageResource(0)
            imgColorNote8.setImageResource(0)
            mSendBoastCard("green_white")
        }
        colorNote7.setOnClickListener {
            imgColorNote1.setImageResource(0)
            imgColorNote2.setImageResource(0)
            imgColorNote3.setImageResource(0)
            imgColorNote4.setImageResource(0)
            imgColorNote5.setImageResource(0)
            imgColorNote6.setImageResource(0)
            imgColorNote7.setImageResource(R.drawable.ic_tick)
            imgColorNote8.setImageResource(0)
            mSendBoastCard("orange")
        }
        colorNote8.setOnClickListener {
            imgColorNote1.setImageResource(0)
            imgColorNote2.setImageResource(0)
            imgColorNote3.setImageResource(0)
            imgColorNote4.setImageResource(0)
            imgColorNote5.setImageResource(0)
            imgColorNote6.setImageResource(0)
            imgColorNote7.setImageResource(0)
            imgColorNote8.setImageResource(R.drawable.ic_tick)
            mSendBoastCard("black")
        }
    }
}