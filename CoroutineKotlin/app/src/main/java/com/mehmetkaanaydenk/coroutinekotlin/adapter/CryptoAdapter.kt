package com.mehmetkaanaydenk.coroutinekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mehmetkaanaydenk.coroutinekotlin.databinding.RecyclerRowBinding
import com.mehmetkaanaydenk.coroutinekotlin.model.Crypto

class CryptoAdapter(var cryptoList: List<Crypto>): RecyclerView.Adapter<CryptoAdapter.ViewHolder>() {
    class ViewHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.currencyTextView.setText(cryptoList.get(position).currency)
        holder.binding.priceTextView.setText(cryptoList.get(position).price)
    }
}