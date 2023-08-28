package com.example.devstreepracticaltask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.devstreepracticaltask.databinding.ItemLocationBinding
import com.example.devstreepracticaltask.room.DatabaseClient
import com.example.devstreepracticaltask.room.LocationModel

class LocationAdapter(
    private val context: Context,
    private var locationList: List<LocationModel>,
    var updateListener: (LocationModel) -> Unit,
    var deleteListener: (Int) -> Unit
) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = locationList[position]

        holder.binding.tvLocation.text = item.name
        holder.binding.tvAddress.text = item.address
        if (item.primary) {
            holder.binding.tvPrimary.visibility = View.VISIBLE
        } else {
            holder.binding.tvPrimary.visibility = View.GONE
        }
        holder.binding.ivEdit.setOnClickListener {
            updateListener.invoke(item)
        }

        holder.binding.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return locationList.size
    }
}