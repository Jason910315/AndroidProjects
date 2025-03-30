package com.example.recyclerviewdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val myDataset : List<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    class MyViewHolder(val textView : TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1,parent,false) as TextView
        return MyViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = myDataset.get(position)
    }
}