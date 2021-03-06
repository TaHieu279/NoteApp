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

    class MyListener constructor(private var context: UpdateActivity, var note: NoteData): DialogInterface.OnClickListener { //S??? ki???n khi Yes | No ??? Dialog:
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
        //L???y ra IdNoteData ???????c g???i...
        //Truy v???n Note t??? Id (Ko g???i c??? note, tr??nh tr?????ng h???p d??? li???u note qu?? l???n s??? g??y ra Exception<TransactionTooLargeException>)
        val idNoteSend = intent.getSerializableExtra("note") as Long
        sendNote = NoteDataDatabase.getInstance(this)!!.noteDataDao().getNoteFromId(idNoteSend)

        //G??n d??? li???u ra c??c tr?????ng trong Activity...
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

    //M??? bottomSheetDialogFragment:
    private fun openChangeColorUpdate() {
        val bottomSheetDialog = MyBottomSheetFragmentColor()
        bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag) //Show dialogFragment.
        bottomSheetDialog.isCancelable = false //Kh??ng cho t???t tr??? khi ???n v??o n??t X.
    }

    //BroadcastReceiver: nh???n d??? li???u, khi c?? s??? ki???n ???????c g???i t???i...
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

    //Kh???i t???o Broadcast khi m??n h??nh start:
    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(myAction)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    //H???y broadcast khi m??n h??nh n??y destroy:
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}
