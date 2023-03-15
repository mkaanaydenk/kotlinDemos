package com.mehmetkaanaydenk.fragmentkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mehmetkaanaydenk.fragmentkotlin.databinding.RecyclerRowBinding
import com.mehmetkaanaydenk.fragmentkotlin.model.Art
import com.mehmetkaanaydenk.fragmentkotlin.view.MainFragmentDirections

class ArtAdapter(val artList: List<Art>): RecyclerView.Adapter<ArtAdapter.ArtHolder>() {

    class ArtHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolder {
        val binding= RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtHolder(binding)
    }

    override fun getItemCount(): Int {
        return artList.size
    }

    override fun onBindViewHolder(holder: ArtHolder, position: Int) {
        holder.binding.recyclerViewTextView.text= artList.get(position).artName

        holder.itemView.setOnClickListener {

            val action= MainFragmentDirections.actionMainFragmentToDetailsFragment("old", artList.get(position).id)
            Navigation.findNavController(it).navigate(action)

        }
    }

}