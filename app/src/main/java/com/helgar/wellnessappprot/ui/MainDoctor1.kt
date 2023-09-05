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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.helgar.wellnessappprot.*
import com.helgar.wellnessappprot.databinding.ActivityMainDoctor1Binding
import com.helgar.wellnessappprot.databinding.ActivityMainUser1Binding
import com.helgar.wellnessappprot.viewmodel.DateViewModel
import com.helgar.wellnessappprot.viewmodel.MainViewModel

class MainDoctor1 : AppCompatActivity(), DateAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var binding: ActivityMainDoctor1Binding // la clase de enlace de vista
    private lateinit var adapter: DateAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(DateViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // infla el diseño usando ViewBinding
        binding = ActivityMainDoctor1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        drawer = binding.doctorDrawer // Asignar la referencia del DrawerLayout a la propiedad drawer

        val toolbar: Toolbar = findViewById(R.id.toolbar_doctor)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view1)
        navigationView.setNavigationItemSelectedListener(this@MainDoctor1)
        //navigationView.bringToFront()

        adapter = DateAdapter(this)
        adapter.setOnItemClickListener(this) // Importante: establecer la instancia actual de la actividad como el listener
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        observeData()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one -> { intent = Intent(this, WellCard::class.java)
            startActivity(intent) }
            R.id.nav_item_two -> Toast.makeText(applicationContext, "Item 2", Toast.LENGTH_SHORT).show()
            R.id.nav_item_three -> Toast.makeText(applicationContext, "Item 3", Toast.LENGTH_SHORT).show()
            R.id.nav_item_six -> Toast.makeText(applicationContext, "Item 6", Toast.LENGTH_SHORT).show()
            R.id.nav_item_seven -> Toast.makeText(applicationContext, "Item 7", Toast.LENGTH_SHORT).show()
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

    override fun onItemClick(position: Int) {
        // Define el comportamiento a ejecutar cuando se hace clic en un elemento
        val clickedItem = adapter.getItem(position)
        val intent = Intent(this, DateInfo::class.java).apply {
            putExtra("nombre", clickedItem.nombre_completo)
            putExtra("nota", clickedItem.nota)
            putExtra("hora", clickedItem.hora)
        }
        startActivity(intent)
        //Toast.makeText(this, "Has hecho clic en ${clickedItem.nombre_completo}", Toast.LENGTH_SHORT).show()
    }

    fun observeData() {
        binding.shimmerViewContainer1.startShimmer()
        viewModel.fetchDateData().observe(this, Observer {
            binding.shimmerViewContainer1.stopShimmer()
            binding.shimmerViewContainer1.visibility = View.GONE
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
}