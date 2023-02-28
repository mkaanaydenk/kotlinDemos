package com.example.roomdatabase.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.roomdatabase.model.Picture
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface PictureDao {

    @Query("SELECT * FROM Picture")
    fun getAll(): Flowable<List<Picture>>

    @Insert
    fun insert(picture: Picture): Completable

    @Delete
    fun delete(picture: Picture): Completable

}