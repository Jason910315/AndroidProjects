package com.example.r76131117_hw2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(data : List<VideoItem>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
    var videos : List<VideoItem> = data
        set(value){
            field = value
            notifyDataSetChanged()
        }
    class ViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val textView = view.findViewById<TextView>(R.id.textView)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.itune_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = videos.get(position).title
        holder.imageView.setImageBitmap(videos.get(position).cover)
    }
}