package com.example.roomdatabase.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.databinding.RecyclerRowBinding
import com.example.roomdatabase.model.Picture
import com.example.roomdatabase.view.DetailsActivity

class PictureAdapter(val pictureList: List<Picture>) :
    RecyclerView.Adapter<PictureAdapter.PictureHolder>() {

    class PictureHolder(val recyclerRowBinding: RecyclerRowBinding) :
        RecyclerView.ViewHolder(recyclerRowBinding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureHolder {
        val recyclerRowBinding =
            RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PictureHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        holder.recyclerRowBinding.recyclerViewTextView.text = pictureList.get(position).name
        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("info","old")
            intent.putExtra("selectedPicture",pictureList.get(position))
            holder.itemView.context.startActivity(intent)

        }
    }

}