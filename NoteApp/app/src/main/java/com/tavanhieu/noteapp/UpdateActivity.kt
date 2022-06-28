package com.tavanhieu.noteapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.tavanhieu.noteapp.msqlite.NoteDataDatabase
import com.tavanhieu.noteapp.myToast.CustomToast
import java.io.ByteArrayInputStream

class UpdateActivity : AppCompatActivity() {
    private lateinit var imgBackPress:      ImageView
    private lateinit var imgDeleteNote:     ImageView
    private lateinit var imgChangeColor:    ImageView
    private lateinit var previewBackUpdate: View
    private lateinit var btnUpdateNote:     Button
    private lateinit var edtTitleNote:      EditText
    private lateinit var edtContentNote:    EditText
    private lateinit var txtDateEditedNote: TextView
    private lateinit var sendNote:          NoteData
    private var imgUpdateNote: ImageView?   = null
    private var mColor = ""

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        anhXa()
        getDataFromEdit()

        imgBackPress.setOnClickListener {
            finish()
        }
        imgDeleteNote.setOnClickListener {
            myDeleteNote()
        }
        btnUpdateNote.setOnClickListener {
            myUpdateNote()
        }
        imgChangeColor.setOnClickListener {
            openChangeColorUpdate()
        }
    }

    private fun myDeleteNote() {
        val myListener = MyListener(this, sendNote)
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_info)
            .setTitle("Delete Note")
            .setMessage("Do you want delete this #Note?")
            .setPositiveButton("Yes", myListener)
            .setNegativeButton("No", myListener)
            .show()
    }

    class MyListener constructor(private var context: UpdateActivity, var note: NoteData): DialogInterface.OnClickListener { //Sự kiện khi Yes | No ở Dialog:
        override fun onClick(dialog: DialogInterface?, which: Int) {
            if(which == -1) {
                NoteDataDatabase.getInstance(context)!!.noteDataDao().deleteOneNote(note.id)
                CustomToast.toast(context, "Delete note successfully.")
                MainActivity.instance.recreate()
                context.finish()
            }
        }
    }

    private fun myUpdateNote() {
        if( edtTitleNote.text.isNullOrEmpty() || edtContentNote.text.isNullOrEmpty() ) {
            Toast.makeText(this@UpdateActivity, "Please enter all note!", Toast.LENGTH_SHORT).show()
        } else {
            sendNote.title = edtTitleNote.text.trim().toString()
            sendNote.content = edtContentNote.text.trim().toString()
            sendNote.mColor = mColor

            //RoomDatabase SQLite:
            try{
                NoteDataDatabase.getInstance(this)!!.noteDataDao().updateNoteData(sendNote)
                CustomToast.toast(this, "Update note successfully.")
                MainActivity.instance.recreate()
                finish()
            } catch (ex: Exception) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun anhXa() {
        imgBackPress = findViewById(R.id.imgArrowBack)
        imgChangeColor = findViewById(R.id.imgChangeColorUpdateBackground)
        imgDeleteNote   = findViewById(R.id.imgDeleteNote)
        btnUpdateNote   = findViewById(R.id.btnUpdateNote)
        edtTitleNote    = findViewById(R.id.edtTitleUpdateNote)
        edtContentNote  = findViewById(R.id.edtContentUpdateNote)
        txtDateEditedNote = findViewById(R.id.txtDateEditedUpdateNote)
        imgUpdateNote     = findViewById(R.id.imgUpdateNote)
        previewBackUpdate = findViewById(R.id.previewBackgroundColorUpdate)
    }

    @SuppressLint("SetTextI18n")
    private fun getDataFromEdit() {
        //Lấy ra IdNoteData được gửi...
        //Truy vấn Note từ Id (Ko gửi cả note, tránh trường hợp dữ liệu note quá lớn sẽ gây ra Exception<TransactionTooLargeException>)
        val idNoteSend = intent.getSerializableExtra("note") as Long
        sendNote = NoteDataDatabase.getInstance(this)!!.noteDataDao().getNoteFromId(idNoteSend)

        //Gán dữ liệu ra các trường trong Activity...
        try {
            edtTitleNote.setText(sendNote.title)
            edtContentNote.setText(sendNote.content)

            if (sendNote.mColor != null && sendNote.mColor != "") {
                mColor = sendNote.mColor!!
                previewBackUpdate.setBackgroundColor(Color.parseColor(mColor))
            }
            if (sendNote.img != null) {
                val inStream = ByteArrayInputStream(sendNote.img)
                val theImage = BitmapFactory.decodeStream(inStream)
                imgUpdateNote?.setImageBitmap(theImage)
                inStream.close()
            }
            txtDateEditedNote.text = "Edited: ${sendNote.mDate}"
        } catch (ex: Exception) {
            CustomToast.toast(this, "Update: ${ex.message}")
        }
    }

    //Mở bottomSheetDialogFragment:
    private fun openChangeColorUpdate() {
        val bottomSheetDialog = MyBottomSheetFragmentColor()
        bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag) //Show dialogFragment.
        bottomSheetDialog.isCancelable = false //Không cho tắt trừ khi ấn vào nút X.
    }

    //BroadcastReceiver: nhận dữ liệu, khi có sự kiện được gọi tới...
    private val myAction = "com.tavanhieu.ACTION_SEND_COLOR"
    private val key  = "colorBackground"
    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(myAction == intent!!.action) {
                when(intent.getStringExtra(key)) {
                    "green"     -> { mColor = "#33691E" }
                    "blue"      -> { mColor = "#4e33ff" }
                    "yellow"    -> { mColor = "#ffd633" }
                    "blue_white" -> { mColor = "#00DDFF" }
                    "purple"    -> { mColor = "#EA60A7" }
                    "green_white" -> { mColor = "#0aebaf"}
                    "orange"    -> { mColor = "#ff7646" }
                    "black"     -> { mColor = "#202734" }
                }
                previewBackUpdate.setBackgroundColor(Color.parseColor(mColor))
            }
        }
    }

    //Khởi tạo Broadcast khi màn hình start:
    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(myAction)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    //Hủy broadcast khi màn hình này destroy:
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}
