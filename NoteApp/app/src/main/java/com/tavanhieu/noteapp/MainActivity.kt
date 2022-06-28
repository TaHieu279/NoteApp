package com.tavanhieu.noteapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tavanhieu.noteapp.fragNote.*
import com.tavanhieu.noteapp.msqlite.NoteDataDatabase
import com.tavanhieu.noteapp.myToast.CustomToast

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var instance: MainActivity
    }
    private lateinit var floatingBar:       FloatingActionButton
    private lateinit var drawerLayoutMain:  DrawerLayout
    private lateinit var imgViewChangeList: ImageView
    private lateinit var imgViewMenu:       ImageView
    private lateinit var searchViewTitle:   SearchView
    private var isListViewNote = false //Biến kiểm tra để chuyển đổi giữa listView và gridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        anhXa()
        instance = this
        /*//SharedPreferences - Lấy dữ liệu đã lưu ở local:
        try {
            ArrNote.arr = DataLocalManager().getListObject()
        } catch (ex: Exception) {}*/

        mOnClick()
        loadFragmentView()
    }

    private fun loadFragmentView() {
        //RoomDatabase SQLite:
        ArrNote.arr = NoteDataDatabase.getInstance(this)!!.noteDataDao().getAllNoteData()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentListViewMain, FragmentListView()).commit()
        if(ArrNote.arr.isEmpty())
            supportFragmentManager.beginTransaction().replace(R.id.fragmentListViewMain, FragmentDefaultView()).commit()
    }

    private fun anhXa() {
        floatingBar         = findViewById(R.id.floatingButton)
        imgViewChangeList   = findViewById(R.id.changeListMain)
        imgViewMenu         = findViewById(R.id.menuToolbarMain)
        drawerLayoutMain    = findViewById(R.id.drawerLayoutMain)
        searchViewTitle     = findViewById(R.id.searchViewNote)
    }

    private fun mOnClick() {
        //Thêm Note mới:
        floatingBar.setOnClickListener {
            startActivity(Intent(this, AddNote::class.java))
        }

        //Thay đổi định dạng list khi nhấn:
        imgViewChangeList.setOnClickListener {
            //Khai báo fragmentTransaction ở đây để tránh trường hợp "commit already called".
            val fragmentTransaction = supportFragmentManager.beginTransaction()

            //isListViewNote = false: ListView visible. Mặc định là listView.
            //isListViewNote = true: GridView visible
            isListViewNote = !isListViewNote
            when {
                ArrNote.arr.size == 0 -> {
                    fragmentTransaction.replace(R.id.fragmentListViewMain, FragmentDefaultView())
                }
                isListViewNote -> {
                    imgViewChangeList.setImageResource(R.drawable.list_grid)
                    fragmentTransaction.replace(R.id.fragmentListViewMain, FragmentGridView())
                }
                else -> {
                    imgViewChangeList.setImageResource(R.drawable.listland)
                    fragmentTransaction.replace(R.id.fragmentListViewMain, FragmentListView())
                }
            }
            fragmentTransaction.commit()
        }

        imgViewMenu.setOnClickListener {
            drawerLayoutMain.openDrawer(GravityCompat.START)
        }

        searchViewTitle.setOnQueryTextFocusChangeListener(object: View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                //Sự kiện setOnQueryTextListener sẽ đc khởi động khi người dùng focus.
                //Tránh tình trạng khi load lại màn hình, sự kiện tự động chạy: arrSearch = null và nhảy vào FragmentDefaultSearch.
                searchViewTitle.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        imgViewChangeList.visibility = View.INVISIBLE
                        val arrSearch = NoteDataDatabase.getInstance(applicationContext)!!.noteDataDao().getSearchNote(newText)
                        if(arrSearch.isNotEmpty()) {
                            supportFragmentManager.beginTransaction().replace(R.id.fragmentListViewMain, FragmentSearchView(arrSearch)).commit()
                        } else {
                            supportFragmentManager.beginTransaction().replace(R.id.fragmentListViewMain, FragmentDefaultSearch()).commit()
                        }
                        return false
                    }
                })
            }
        })

        searchViewTitle.setOnCloseListener {
            loadFragmentView()
            imgViewChangeList.visibility = View.VISIBLE
            false
        }
    }
}