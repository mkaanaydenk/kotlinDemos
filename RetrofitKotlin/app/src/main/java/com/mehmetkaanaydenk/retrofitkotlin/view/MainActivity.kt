package com.mehmetkaanaydenk.retrofitkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RecoverySystem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mehmetkaanaydenk.retrofitkotlin.R
import com.mehmetkaanaydenk.retrofitkotlin.adapter.CryptoAdapter
import com.mehmetkaanaydenk.retrofitkotlin.databinding.ActivityMainBinding
import com.mehmetkaanaydenk.retrofitkotlin.model.CryptoModel
import com.mehmetkaanaydenk.retrofitkotlin.service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CryptoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)
        //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json
        compositeDisposable= CompositeDisposable()

        val layoutManager: RecyclerView.LayoutManager= LinearLayoutManager(this)
        binding.recyclerView.layoutManager= layoutManager

        loadData()

    }


    private fun loadData(){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)

        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse)
        )


    }

    fun handleResponse(cryptoList: List<CryptoModel>){

        cryptoModels= ArrayList(cryptoList)

        adapter= CryptoAdapter(cryptoModels!!)
        binding.recyclerView.adapter= adapter

    }

}