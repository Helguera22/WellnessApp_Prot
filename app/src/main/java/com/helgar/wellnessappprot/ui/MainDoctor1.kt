package com.helgar.wellnessappprot.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.helgar.wellnessappprot.*
import com.helgar.wellnessappprot.databinding.ActivityMainDoctor1Binding
import com.helgar.wellnessappprot.databinding.ActivityMainUser1Binding
import com.helgar.wellnessappprot.viewmodel.DateViewModel
import com.helgar.wellnessappprot.viewmodel.MainViewModel

class MainDoctor1 : AppCompatActivity(), DateAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainDoctor1Binding // la clase de enlace de vista
    private lateinit var adapter: DateAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(DateViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // infla el diseño usando ViewBinding
        binding = ActivityMainDoctor1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DateAdapter(this)
        adapter.setOnItemClickListener(this) // Importante: establecer la instancia actual de la actividad como el listener
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        observeData()
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