package com.example.roomdatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Picture(

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var descriptions: String,

    @ColumnInfo(name = "image")
    var image: ByteArray? = null

) :java.io.Serializable {

    @PrimaryKey(autoGenerate = true)
    var id = 0

}