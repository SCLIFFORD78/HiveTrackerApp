package org.wit.hivetrackerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.adapters.HiveTrackerAdapter
import org.wit.hivetrackerapp.adapters.HiveTrackerListener
import org.wit.hivetrackerapp.databinding.ActivityHiveTrackerListBinding
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.HiveModel

class HiveTrackerListActivity : AppCompatActivity() , HiveTrackerListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityHiveTrackerListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHiveTrackerListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadHives()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, HiveTrackerActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHiveClick(hive: HiveModel) {
        val launcherIntent = Intent(this, HiveTrackerActivity::class.java)
        launcherIntent.putExtra("hive_edit", hive)
        refreshIntentLauncher.launch(launcherIntent)
    }



    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadHives() }
    }

    private fun loadHives() {
        showHives(app.hives.findAll())
    }

    fun showHives (hives: List<HiveModel>) {
        binding.recyclerView.adapter = HiveTrackerAdapter(hives, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}

