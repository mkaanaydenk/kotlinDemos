package com.mehmetkaanaydenk.retrofitkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mehmetkaanaydenk.retrofitkotlin.databinding.RecyclerRowBinding
import com.mehmetkaanaydenk.retrofitkotlin.model.CryptoModel

class CryptoAdapter(val cryptoList: ArrayList<CryptoModel>): RecyclerView.Adapter<CryptoAdapter.CryptoHolder>() {
    class CryptoHolder(val recyclerRowBinding: RecyclerRowBinding): RecyclerView.ViewHolder(recyclerRowBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoHolder {
        val binding= RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CryptoHolder(binding)
    }

    override fun getItemCount(): Int {
       return cryptoList.size
    }

    override fun onBindViewHolder(holder: CryptoHolder, position: Int) {
        holder.recyclerRowBinding.nameText.setText(cryptoList.get(position).currency)
        holder.recyclerRowBinding.priceText.setText(cryptoList.get(position).price)
    }
}