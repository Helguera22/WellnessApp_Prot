package com.helgar.wellnessappprot.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.helgar.wellnessappprot.DateDetails
import com.helgar.wellnessappprot.DocInfo
import com.helgar.wellnessappprot.MainAdapter
import com.helgar.wellnessappprot.R
import com.helgar.wellnessappprot.viewmodel.MainViewModel
import com.helgar.wellnessappprot.databinding.ActivityMainUser1Binding

class MainUser1 : AppCompatActivity(), MainAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var binding: ActivityMainUser1Binding // la clase de enlace de vista
    private lateinit var adapter: MainAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java)}
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_user)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // infla el diseño usando ViewBinding
        binding = ActivityMainUser1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener las SharedPreferences
        sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE)

        adapter = MainAdapter(this)
        adapter.setOnItemClickListener(this) // Importante: establecer la instancia actual de la actividad como el listener
        binding.recyclerView1.layoutManager = LinearLayoutManager(this)
        binding.recyclerView1.adapter = adapter
        binding.recyclerView1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        observeData()
    }

    override fun onItemClick(position: Int) {
        // Define el comportamiento a ejecutar cuando se hace clic en un elemento
        // por ejemplo, abrir una nueva actividad o fragmento con detalles del doctor
        val cedulaDoctor = sharedPreferences.getString("cedulaDoctor", "")
        val nssPaciente = sharedPreferences.getString("nssPaciente", "")
        val clickedItem = adapter.getItem(position)
        val intent = Intent(this, DocInfo::class.java).apply {
            putExtra("nombre", clickedItem.nombre_completo)
            putExtra("especialidad", clickedItem.especialidad)
            putExtra("cedulaDoctor", cedulaDoctor)
            putExtra("nssPaciente", nssPaciente)
        }
        startActivity(intent)
        //Toast.makeText(this, "Has hecho clic en ${clickedItem.nombre_completo}", Toast.LENGTH_SHORT).show()
    }

    fun observeData() {
        binding.shimmerViewContainer.startShimmer()
        viewModel.fetchDocData().observe(this, Observer {
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one -> Toast.makeText(this, "Item 1", Toast.LENGTH_SHORT).show()
            R.id.nav_item_two -> Toast.makeText(this, "Item 2", Toast.LENGTH_SHORT).show()
            R.id.nav_item_three -> Toast.makeText(this, "Item 3", Toast.LENGTH_SHORT).show()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}