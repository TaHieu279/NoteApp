package com.tavanhieu.noteapp.myToast

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.tavanhieu.noteapp.R

class CustomToast {
    companion object {
        fun toast(context: Context, value: String) {
            val mToast = Toast(context)
            val mView = LayoutInflater.from(context).inflate(R.layout.layout_custom_toast, null)
            val txtMessage = mView.findViewById<TextView>(R.id.txtMessageToast)
            txtMessage.text = value

            mToast.view = mView
            mToast.setGravity(Gravity.BOTTOM, 0, 27)
            mToast.duration = Toast.LENGTH_SHORT
            mToast.show()
        }
    }
}