package com.tavanhieu.noteapp.msqlite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tavanhieu.noteapp.NoteData

@Database(entities = [NoteData::class], version = 1)
abstract class NoteDataDatabase: RoomDatabase() {
    abstract fun noteDataDao(): NoteDataDAO

    companion object {
        private const val databaseName = "dbnotedata.db"
        private var instance: NoteDataDatabase? = null

        fun getInstance(context: Context): NoteDataDatabase? {
            if(instance == null) {
                instance = Room.databaseBuilder(context, NoteDataDatabase::class.java, databaseName).allowMainThreadQueries().build()
            }
            return instance
        }
    }
}