package com.tavanhieu.noteapp.madapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tavanhieu.noteapp.NoteData
import com.tavanhieu.noteapp.R
import com.tavanhieu.noteapp.UpdateActivity
import java.io.ByteArrayInputStream

class AdapterAddNote constructor(var context: Context, var layout: Int, var arrNote: MutableList<NoteData>): BaseAdapter() {

    override fun getCount(): Int {
        return arrNote.size
    }

    override fun getItem(position: Int): Any {
        return arrNote[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder {
        lateinit var imgHinh: ImageView
        lateinit var txtTitle: TextView
        lateinit var txtContent: TextView
        lateinit var txtDate: TextView
        lateinit var layoutItemNoteAdd: LinearLayout
    }

    @SuppressLint("SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var mView = convertView
        val mholder: ViewHolder

        if(mView == null) {
            mholder = ViewHolder()
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mView = inflater.inflate(layout, null)

            mholder.imgHinh         = mView.findViewById(R.id.imgNoteAdapter)
            mholder.txtTitle        = mView.findViewById(R.id.txtTitleNoteAdapter)
            mholder.txtContent      = mView.findViewById(R.id.txtContentNoteAdapter)
            mholder.txtDate         = mView.findViewById(R.id.txtDateNoteAdapter)
            mholder.layoutItemNoteAdd = mView.findViewById(R.id.layout_item_note_add)
            mView.tag = mholder
        } else {
            mholder = mView.tag as ViewHolder
        }

        val res = arrNote[position]

        try {
            //set Image: Chuyển ảnh từ byteArray về lại Bitmap:
            if (res.img != null)  {
                val imageStream = ByteArrayInputStream(res.img)
                val theImage = BitmapFactory.decodeStream(imageStream)
                mholder.imgHinh.setImageBitmap(theImage)
                mholder.imgHinh.visibility = View.VISIBLE
                imageStream.close()
            } else {
                mholder.imgHinh.visibility = View.GONE
            }
            //set Background:
            if(res.mColor != null && res.mColor !=  "") {
                mholder.layoutItemNoteAdd.setBackgroundColor(Color.parseColor(res.mColor))
            }
        } catch (ex: Exception) {
            Toast.makeText(context, "Adapter: " + ex.message, Toast.LENGTH_SHORT).show()
        }

        mholder.txtTitle.text   = res.title
        mholder.txtContent.text = res.content
        mholder.txtDate.text    = res.mDate

        mholder.layoutItemNoteAdd.setOnClickListener {
            onClickGoToAddNote(res)
        }

        return mView
    }

    private fun onClickGoToAddNote(res: NoteData) {
        val intent = Intent(context, UpdateActivity::class.java)
        intent.putExtra("note", res.id)
        context.startActivity(intent)
    }
}
