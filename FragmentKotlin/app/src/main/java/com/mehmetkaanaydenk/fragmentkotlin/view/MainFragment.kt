package com.mehmetkaanaydenk.fragmentkotlin.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.room.Room
import com.mehmetkaanaydenk.fragmentkotlin.adapter.ArtAdapter
import com.mehmetkaanaydenk.fragmentkotlin.databinding.FragmentMainBinding
import com.mehmetkaanaydenk.fragmentkotlin.model.Art
import com.mehmetkaanaydenk.fragmentkotlin.roomdb.ArtDao
import com.mehmetkaanaydenk.fragmentkotlin.roomdb.ArtDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class MainFragment : Fragment() {

    private lateinit var adapter: ArtAdapter
    private lateinit var db: ArtDatabase
    private lateinit var artDao: ArtDao
    private val compositeDisposable= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        db= Room.databaseBuilder(requireContext().applicationContext,ArtDatabase::class.java,"Arts").build()
        artDao= db.artDao()

    }

    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSql()


    }

    fun getSql(){

        compositeDisposable.add(artDao.getAlll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse)


        )

    }

    fun handleResponse( artList: List<Art>){

        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        adapter= ArtAdapter(artList)
        binding.recyclerView.adapter = adapter

    }


}