package com.example.fittrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fittrack.R.layout.each_row
import com.example.fittrack.log.DatabaseTable

class MyAdapter(var workoutList : List<DatabaseTable>, private val listener: OnItemClickListener): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onDeleteButtonClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(each_row, parent, false))
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
    ) {
        val currentItem = workoutList[position]
        holder.itemView.findViewById<TextView>(R.id.tVName).text = currentItem.exName
        holder.itemView.findViewById<TextView>(R.id.tVDuration).text= currentItem.duration.toString()
        holder.itemView.findViewById<TextView>(R.id.tVCalories).text = currentItem.calorie.toString()
        holder.itemView.findViewById<TextView>(R.id.tVDate).text = currentItem.date
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemButton = itemView.findViewById<ImageButton>(R.id.btnDel)
        init {
            itemButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteButtonClick(position)
                }
            }
        }

    }
    fun setData(workout : List<DatabaseTable>) {
        this.workoutList = workout
        notifyDataSetChanged()
    }
}