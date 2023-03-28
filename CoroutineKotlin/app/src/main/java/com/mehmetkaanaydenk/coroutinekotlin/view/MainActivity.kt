package com.mehmetkaanaydenk.coroutinekotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.mehmetkaanaydenk.coroutinekotlin.R
import com.mehmetkaanaydenk.coroutinekotlin.adapter.CryptoAdapter
import com.mehmetkaanaydenk.coroutinekotlin.databinding.ActivityMainBinding
import com.mehmetkaanaydenk.coroutinekotlin.model.Crypto
import com.mehmetkaanaydenk.coroutinekotlin.service.CryptoAPI
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<Crypto>? = null
    private var job: Job? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CryptoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadData()
    }

    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)

        job = CoroutineScope(Dispatchers.IO).launch {

            val response = retrofit.getAll()

            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {

                    response.body()?.let {

                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            adapter = CryptoAdapter(it)
                            binding.recyclerView.adapter = adapter
                        }

                    }

                }

            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}