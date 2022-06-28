package com.tavanhieu.noteapp.msqlite

import androidx.room.*
import com.tavanhieu.noteapp.NoteData

@Dao
interface NoteDataDAO {
    @Insert
    fun insertNoteData(noteData: NoteData)

    @Update
    fun updateNoteData(mNoteData: NoteData)

    @Query("Select * From MyNote")
    fun getAllNoteData(): MutableList<NoteData>

    @Query("Delete from MyNote where id = :mid")
    fun deleteOneNote(mid: Long)

    @Query("Select * from MyNote where title = :mTitle")
    fun getSearchNote(mTitle: String?): MutableList<NoteData>

    @Query("Select * from MyNote where id = :mId")
    fun getNoteFromId(mId: Long): NoteData
}