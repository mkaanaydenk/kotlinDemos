package com.mehmetkaanaydenk.fragmentkotlin.roomdb

import androidx.room.*
import com.mehmetkaanaydenk.fragmentkotlin.model.Art
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ArtDao {

    @Query(value = "SELECT * FROM Art")
    fun getAlll(): Flowable<List<Art>>

    @Query(value = "SELECT * FROM Art WHERE id= :id")
    fun getArtById(id: Int): Flowable<Art>

    @Insert
    fun insert (art: Art): Completable

    @Delete
    fun delete (art: Art): Completable

}