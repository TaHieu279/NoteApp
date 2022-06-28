package com.tavanhieu.noteapp.dataLocal

import android.app.Application

class KhoiTaoDataLocalManager: Application() {
    //Xác định xem thành phần nào được sử dụng trong phạm vi toàn bộ project:
    override fun onCreate() {
        super.onCreate()
        mSelf = this  //Trả về màn hình getApplicationContext()...
    }

    companion object {
        private var mSelf: KhoiTaoDataLocalManager? = null
        fun self(): KhoiTaoDataLocalManager? {
            return mSelf //trả về 1 context...
        }
    }
}