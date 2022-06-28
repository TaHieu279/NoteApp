package com.tavanhieu.noteapp

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "MyNote")
data class NoteData(@PrimaryKey(autoGenerate = true) @NonNull var id: Long,
                    var img: ByteArray? = null,
                    var title: String = "",
                    var content: String = "",
                    var mDate: String = "",
                    var mColor: String?) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteData

        if (id != other.id) return false
        if (img != null) {
            if (other.img == null) return false
            if (!img.contentEquals(other.img)) return false
        } else if (other.img != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (img?.contentHashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + mDate.hashCode()
        result = 31 * result + (mColor?.hashCode() ?: 0)
        return result
    }
}