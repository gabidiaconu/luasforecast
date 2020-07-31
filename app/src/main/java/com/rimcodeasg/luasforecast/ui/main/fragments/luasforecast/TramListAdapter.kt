package com.rimcodeasg.luasforecast.ui.main.fragments.luasforecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rimcodeasg.luasforecast.data.models.Tram
import com.rimcodeasg.luasforecast.databinding.TramRvItemBinding

class TramListAdapter(val recyclerViewItemClickListener: IRecyclerViewItemClickListener) : ListAdapter<Tram, TramListAdapter.TramViewHolder>(
    Companion
) {

    class TramViewHolder(val binding : TramRvItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindInterface(tram: Tram,
                        recyclerViewItemClickListener: IRecyclerViewItemClickListener){
            itemView.setOnClickListener {
                recyclerViewItemClickListener.onRecyclerViewItemClicked(tram)
            }
        }
    }

    companion object: DiffUtil.ItemCallback<Tram>() {
        override fun areItemsTheSame(oldItem: Tram, newItem: Tram): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: Tram, newItem: Tram): Boolean =
            oldItem.dueMins == newItem.dueMins && oldItem.destination == newItem.destination
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TramViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TramRvItemBinding.inflate(layoutInflater)

        return TramViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TramViewHolder, position: Int) {
        val currentTram = getItem(position)
        holder.binding.tram = currentTram
        holder.binding.executePendingBindings()

        holder.bindInterface(currentTram, recyclerViewItemClickListener)
    }

}