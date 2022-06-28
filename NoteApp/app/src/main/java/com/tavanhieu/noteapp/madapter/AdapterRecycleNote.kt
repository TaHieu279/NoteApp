package com.tavanhieu.noteapp.madapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tavanhieu.noteapp.NoteData
import com.tavanhieu.noteapp.R
import com.tavanhieu.noteapp.UpdateActivity
import java.io.ByteArrayInputStream

class AdapterRecycleNote constructor(var context: Context): RecyclerView.Adapter<AdapterRecycleNote.MyViewHolder>() {
    private lateinit var arrNote: MutableList<NoteData>

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgHinh: ImageView?     = itemView.findViewById(R.id.imgNoteAdapter)
        var txtTitle: TextView      = itemView.findViewById(R.id.txtTitleNoteAdapter)
        var txtContent: TextView    = itemView.findViewById(R.id.txtContentNoteAdapter)
        var txtDate: TextView       = itemView.findViewById(R.id.txtDateNoteAdapter)
        var layoutItemNoteAdd: LinearLayout = itemView.findViewById(R.id.layout_item_note_add)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(arrNote: MutableList<NoteData>) {
        this.arrNote = arrNote
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_view, parent, false)
        return MyViewHolder(mView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val res = arrNote[position]

        try {
            if (res.img != null)  {
                //set Image: Chuyển ảnh từ byteArray về lại Bitmap:
                val imageStream = ByteArrayInputStream(res.img)
                val theImage = BitmapFactory.decodeStream(imageStream)
                holder.imgHinh?.setImageBitmap(theImage)
                imageStream.close()
            }
            //set Background:
            if(res.mColor != null && res.mColor !=  "") {
                holder.layoutItemNoteAdd.setBackgroundColor(Color.parseColor(res.mColor))
            }
        } catch (ex: Exception) {
            Toast.makeText(context, "Adapter: " + ex.message, Toast.LENGTH_SHORT).show()
        }

        holder.txtTitle.text   = res.title
        holder.txtContent.text = res.content
        holder.txtDate.text    = res.mDate

        holder.layoutItemNoteAdd.setOnClickListener {
            onClickGoToAddNote(res)
        }
    }

    private fun onClickGoToAddNote(res: NoteData) {
        val intent = Intent(context, UpdateActivity::class.java)
        intent.putExtra("note", res.id)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        if(arrNote.isNotEmpty()) {
            return arrNote.size
        }
        return 0
    }
}