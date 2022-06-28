package com.tavanhieu.noteapp.dataLocal

import android.content.Context
import com.google.gson.Gson
import com.tavanhieu.noteapp.NoteData
import org.json.JSONArray

class DataLocalManager {
    //KhoiTaoDataLocalManager.self()!! : Lấy ra context để gọi được hàm getSharedPreferences()
    private val mShared = KhoiTaoDataLocalManager.self()!!.getSharedPreferences("MY_SHARED", Context.MODE_PRIVATE)

    fun setListObject(arrNote: ArrayList<NoteData>) {
        //Chuyển 1 list object về JsonArray và convert về dạng String để lưu trữ ...
        val strJsonArray = Gson().toJsonTree(arrNote).asJsonArray.toString()
        mShared.edit().putString("SHARED_LIST_OBJECT", strJsonArray).apply()
    }

    fun getListObject(): ArrayList<NoteData> {
        val arrNote = ArrayList<NoteData>()
        //Lấy ra List object đã put vào ở dạng stringJsonArray
        val strJSONArray = mShared.getString("SHARED_LIST_OBJECT", "")
        //Chuyển chuỗi dạng StringJsonArray về JsonArray:
        val jsonArray = JSONArray(strJSONArray)

        //Từ mảng jsonArray đã lưu, ta tách ra thành từng JsonObject để chuyển về 1 đối tượng object, sau đó add ngược lại list:
        for (i in 0 until jsonArray.length()) {
            //jsonArray.getJSONObject(i).toString(): Lấy ra JsonObject
            val noteData: NoteData = Gson().fromJson(jsonArray.getJSONObject(i).toString(), NoteData::class.java)
            arrNote.add(noteData)
        }
        return arrNote
    }
}