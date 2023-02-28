package com.example.roomdatabase.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomdatabase.model.Picture

@Database(entities = [Picture::class], version = 1)
abstract class PictureDatabase : RoomDatabase() {
    abstract fun pictureDao(): PictureDao
}
