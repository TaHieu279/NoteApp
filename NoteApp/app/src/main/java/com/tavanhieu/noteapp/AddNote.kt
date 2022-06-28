package com.tavanhieu.noteapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.tavanhieu.noteapp.msqlite.NoteDataDatabase
import com.tavanhieu.noteapp.myToast.CustomToast
import gun0912.tedbottompicker.TedBottomPicker
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNote : AppCompatActivity() {
    private lateinit var imgCamera:     ImageView
    private lateinit var imgGallery:    ImageView
    private lateinit var imgBackPress:  ImageView
    private lateinit var imgColor:      ImageView
    private lateinit var preViewBack:   View
    private lateinit var edtTitleNote:  EditText
    private lateinit var edtContentNote: EditText
    private lateinit var btnSave:        Button
    private lateinit var txtDateEditedNote: TextView
    private lateinit var imageUriTakePhoto: Uri
    private var mCurrentDay = ""
    private var mColor = ""
    private var mImagePath: ByteArray? = null
    private var imgAddNote: ImageView? = null

    private val myTakePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) {
            imgAddNote!!.setImageURI(imageUriTakePhoto)
        }
    }

    //BroadcastReceiver: nhận dữ liệu, khi có sự kiện được gọi tới...
    private val myAction = "com.tavanhieu.ACTION_SEND_COLOR"
    private val key  = "colorBackground"
    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(myAction == intent!!.action) {
                when(intent.getStringExtra(key)) {
                    "green" -> {
                        mColor = "#33691E"
                        preViewBack.setBackgroundColor(Color.parseColor("#33691E"))
                    }
                    "blue" -> {
                        mColor = "#4e33ff"
                        preViewBack.setBackgroundColor(Color.parseColor("#4e33ff"))
                    }
                    "yellow" -> {
                        mColor = "#ffd633"
                        preViewBack.setBackgroundColor(Color.parseColor("#ffd633"))
                    }
                    "blue_white" -> {
                        mColor = "#00DDFF"
                        preViewBack.setBackgroundColor(Color.parseColor("#00DDFF"))
                    }
                    "purple" -> {
                        mColor = "#EA60A7"
                        preViewBack.setBackgroundColor(Color.parseColor("#EA60A7"))
                    }
                    "green_white" -> {
                        mColor = "#0aebaf"
                        preViewBack.setBackgroundColor(Color.parseColor("#0aebaf"))
                    }
                    "orange" -> {
                        mColor = "#ff7646"
                        preViewBack.setBackgroundColor(Color.parseColor("#ff7646"))
                    }
                    "black" -> {
                        mColor = "#202734"
                        preViewBack.setBackgroundColor(Color.parseColor("#202734"))
                    }
                    else -> {
                        mColor = "#00B8D4"
                        preViewBack.setBackgroundColor(Color.parseColor("#00B8D4"))
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(myAction)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        anhXa()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        mCurrentDay = dateFormat.format(Date()).toString()
        txtDateEditedNote.text = "Edited: $mCurrentDay"

        onClickActivityAddNote()
    }

    private fun onClickActivityAddNote() {
        //Trở về MainActivity:
        imgBackPress.setOnClickListener {
            finish()
        }
        //Lưu note:
        btnSave.setOnClickListener {
            saveData()
        }
        //Mở camera để chụp ảnh:
        imgCamera.setOnClickListener {
            openCamera()
        }
        //Lấy ảnh từ thư viện:
        imgGallery.setOnClickListener {
            openGallery()
        }
        //Đổi màu background:
        imgColor.setOnClickListener {
            openChangeBackgroundColor()
        }
    }

    private fun openChangeBackgroundColor() {
        val bottomSheetDialog = MyBottomSheetFragmentColor()
        bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
        bottomSheetDialog.isCancelable = false
    }

    private fun saveData() {
        if( edtTitleNote.text.isNullOrEmpty() || edtContentNote.text.isNullOrEmpty() ) {
            CustomToast.toast(this, "Please enter all note!")
        } else {
            /*ArrNote.arr.add(NoteData(imgAddNote, edtTitleNote.text.toString(), edtContentNote.text.toString(), mCurrentDay))
            //SharedPreferences - Lưu dl vào điện thoại:
            DataLocalManager().setListObject(ArrNote.arr)*/

            //RoomDatabase SQLite:
            try{
                NoteDataDatabase
                    .getInstance(this)!!
                    .noteDataDao()
                    .insertNoteData(NoteData(0, mImagePath, edtTitleNote.text.toString(), edtContentNote.text.toString(), mCurrentDay, mColor))
                MainActivity.instance.recreate()
                finish()
                CustomToast.toast(this, "Add note successfully.")
            } catch (ex: Exception) {
                CustomToast.toast(this, "AddNote: ${ex.message}")
            }
        }
    }

    private fun openCamera() {//đi cop nên chưa hiểu lệnh :))
        //Sau khi chụp xong, Uri ảnh sẽ đc lưu vào imageUriTakePhoto...
        imageUriTakePhoto = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())!!
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriTakePhoto)

        myTakePicture.launch(cameraIntent)
    }

    private fun openGallery() {
        //Yêu cầu quyền truy cập từ Android 6.0 trở lên:
        val permissionListener: PermissionListener = object : PermissionListener {
            //Allow:
            override fun onPermissionGranted() {
                openTedBottomPicker()
            }
            //Denied:
            override fun onPermissionDenied(deniedPermissions: List<String>) {
                CustomToast.toast(this@AddNote, "Denied open $deniedPermissions")
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission, you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }

    private fun openTedBottomPicker() {
        TedBottomPicker.with(this).show {
            try {
                //Set Bitmap cho ImageAddNote:
                imgAddNote!!.setImageURI(it)

                //Convert Uri về Bitmap:
                val inputStream = contentResolver.openInputStream(it)
                val bitmapFolder = BitmapFactory.decodeStream(inputStream)

                //Chuyển ảnh Bitmap về byteArray để lưu trữ:
                val stream = ByteArrayOutputStream()
                bitmapFolder.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                mImagePath = stream.toByteArray()

                stream.close()
            } catch (ex: Exception) {}
        }
    }

    private fun anhXa() {
        imgBackPress        = findViewById(R.id.imgArrowBack)
        imgCamera           = findViewById(R.id.imgCamera)
        imgGallery          = findViewById(R.id.imgAlbum)
        btnSave             = findViewById(R.id.btnSaveNote)
        imgColor            = findViewById(R.id.imgChangeColorBackground)
        edtTitleNote        = findViewById(R.id.edtTitleNote)
        edtContentNote      = findViewById(R.id.edtContentNote)
        txtDateEditedNote   = findViewById(R.id.txtDateEditedNote)
        imgAddNote          = findViewById(R.id.imgAddNote)
        preViewBack         = findViewById(R.id.previewBackgroundColor)
    }
}