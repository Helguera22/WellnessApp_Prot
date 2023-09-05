package com.helgar.wellnessappprot.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.helgar.wellnessappprot.DocInfo
import com.helgar.wellnessappprot.MainAdapter
import com.helgar.wellnessappprot.R
import com.helgar.wellnessappprot.UserLog
import com.helgar.wellnessappprot.databinding.ActivityMainUser1Binding
import com.helgar.wellnessappprot.viewmodel.MainViewModel

class MainUser1 : AppCompatActivity(), MainAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var binding: ActivityMainUser1Binding
    private lateinit var adapter: MainAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainUser1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        drawer = binding.drawerLayout // Asignar la referencia del DrawerLayout a la propiedad drawer

        val toolbar: Toolbar = findViewById(R.id.toolbar_user)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this@MainUser1)
        //navigationView.bringToFront()
        sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE)

        // Obtener una referencia al TextView en el encabezado del Navigation Drawer
        val headerView = navigationView.getHeaderView(0)
        val nombreTextView = headerView.findViewById<TextView>(R.id.nav_header_textview)

        // Obtener el correo electrónico del usuario
        val userEmail = sharedPreferences.getString("user_email", "correo")

        // Obtener el nombre del paciente desde la base de datos
        if (userEmail != null) {
            obtenerNombreDesdeBaseDeDatos(userEmail) { nombre ->
                // Actualizar el texto del TextView con el nombre obtenido
                nombreTextView.text = nombre
            }
        }

        adapter = MainAdapter(this)
        adapter.setOnItemClickListener(this)
        binding.recyclerView1.layoutManager = LinearLayoutManager(this)
        binding.recyclerView1.adapter = adapter
        binding.recyclerView1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        observeData()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one -> Toast.makeText(applicationContext, "Item 1", Toast.LENGTH_SHORT).show()
            R.id.nav_item_two -> Toast.makeText(applicationContext, "Item 2", Toast.LENGTH_SHORT).show()
            R.id.nav_item_three -> Toast.makeText(applicationContext, "Item 3", Toast.LENGTH_SHORT).show()
            R.id.nav_item_six -> Toast.makeText(applicationContext, "Item 6", Toast.LENGTH_SHORT).show()
            R.id.nav_item_seven -> confirmLogout()
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
        val clickedItem = adapter.getItem(position)
        val intent = Intent(this, DocInfo::class.java).apply {
            putExtra("nombre", clickedItem.nombre_completo)
            putExtra("especialidad", clickedItem.especialidad)
        }
        startActivity(intent)
    }

    private fun observeData() {
        binding.shimmerViewContainer.startShimmer()
        viewModel.fetchDocData().observe(this, Observer {
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun confirmLogout() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { dialog, which ->
                // Eliminar el estado de la sesión guardado
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", false)
                editor.apply()

                // Redirigir a la actividad de inicio de sesión
                val intent = Intent(this, UserLog::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun obtenerNombreDesdeBaseDeDatos(userEmail: String, callback: (String) -> Unit) {
        val url = "http://ictuscorps.atwebpages.com/anime-main/php/getNombre_paciente.php?correo_electronico=$userEmail"
        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val nombre = response.getString("nombre")

                runOnUiThread {
                    // Actualizar el TextView en el Navigation Drawer con el nombre obtenido
                    val navigationView = findViewById<NavigationView>(R.id.nav_view)
                    val headerView = navigationView.getHeaderView(0)
                    val nombreTextView = headerView.findViewById<TextView>(R.id.nav_header_textview)
                    nombreTextView.text = nombre

                    // Llamar a la función de devolución de llamada con el nombre obtenido
                    callback(nombre)
                }
            },
            Response.ErrorListener { error ->
                // Manejo de errores
            }
        )

        queue.add(jsonObjectRequest)
    }

}