package org.wit.hivetrackerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.hivetrackerapp.databinding.ActivityHiveTrackerListBinding
import org.wit.hivetrackerapp.databinding.CardHiveBinding
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.HiveModel

class HiveTrackerListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityHiveTrackerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHiveTrackerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = HiveAdapter(app.hives)
    }
}

class HiveAdapter constructor(private var hives: List<HiveModel>) :
    RecyclerView.Adapter<HiveAdapter.MainHolder>() {

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
        }
    }
}