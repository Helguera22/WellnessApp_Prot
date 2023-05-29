package com.helgar.wellnessappprot

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.helgar.wellnessappprot.databinding.DateItemRowsBinding
import com.helgar.wellnessappprot.databinding.DoctorItemRowsBinding

class DateAdapter(private val context: Context): RecyclerView.Adapter<DateAdapter.MainViewHolder>() {

    private var dataList = mutableListOf<Cita>()
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setListData(data:MutableList<Cita>) {
        dataList = data
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = DateItemRowsBinding.inflate(LayoutInflater.from(context), parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cita = dataList[position]
        holder.bindView(cita)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    inner class MainViewHolder(private val binding: DateItemRowsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(cita:Cita) {
            binding.txtName.text = cita.nombre_completo
            binding.txtReason.text = cita.nota
            binding.txtDate.text = cita.hora
        }
    }

    fun getItem(position: Int): Cita {
        return dataList[position]
    }
}