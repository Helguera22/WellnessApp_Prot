package com.helgar.wellnessappprot

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.helgar.wellnessappprot.databinding.DoctorItemRowsBinding
import com.helgar.wellnessappprot.dataclasses.Doctor

class MainAdapter(private val context: Context): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var dataList = mutableListOf<Doctor>()
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setListData(data:MutableList<Doctor>) {
        dataList = data
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = DoctorItemRowsBinding.inflate(LayoutInflater.from(context), parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val doctor = dataList[position]
        holder.bindView(doctor)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    inner class MainViewHolder(private val binding: DoctorItemRowsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(doctor: Doctor) {
            Glide.with(context).load(doctor.imageUrl).into(binding.circleimageview)
            binding.txtTitle.text = doctor.nombre_completo
            binding.txtDescr.text = doctor.especialidad
        }
    }

    fun getItem(position: Int): Doctor {
        return dataList[position]
    }
}