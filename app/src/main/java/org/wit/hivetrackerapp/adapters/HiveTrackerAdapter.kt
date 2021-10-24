package org.wit.hivetrackerapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.hivetrackerapp.databinding.CardHiveBinding
import org.wit.hivetrackerapp.models.HiveModel

interface HiveTrackerListener {
    fun onHiveClick(hive: HiveModel)
}

class HiveTrackerAdapter(private var hives: List<HiveModel>) :
    RecyclerView.Adapter<HiveTrackerAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardHiveBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hive = hives[holder.adapterPosition]
        holder.bind(hive)
    }

    override fun getItemCount(): Int = hives.size

    class MainHolder(private val binding : CardHiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hive: HiveModel) {
            binding.hiveTitle.text = hive.title
            binding.description.text = hive.description
            binding.hiveImage.setImageURI(hive.image)
        }
    }
}

