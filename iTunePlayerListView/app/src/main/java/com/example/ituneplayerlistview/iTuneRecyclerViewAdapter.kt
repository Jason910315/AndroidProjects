package com.example.ituneplayerlistview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ituneplayer.SongItem
// 11.
class iTuneRecyclerViewAdapter(data : List<SongItem>,val listener : RecyclerViewClickListener?=null)
    : RecyclerView.Adapter<iTuneRecyclerViewAdapter.ViewHolder>(){
    // 22.
    interface RecyclerViewClickListener{
        fun onClick(position : Int)
    }
    var songs : List<SongItem> = data
        set(value){
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val textView = view.findViewById<TextView>(R.id.textView)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itune_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = songs.get(position).title
        holder.imageView.setImageBitmap(songs.get(position).cover)
        // 33.
        holder.view.setOnClickListener {
            listener?.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

}