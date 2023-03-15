package com.mehmetkaanaydenk.fragmentkotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Art (

    @ColumnInfo(name = "name")
    var artName: String,

    @ColumnInfo(name = "year")
    var artYear: String,

    @ColumnInfo(name = "image")
    var image: ByteArray){

    @PrimaryKey(autoGenerate = true)
    var id = 0

}