package com.example.ituneplayerlistview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.ituneplayerlistview.SongItem

// 要使用 Adapter 物件要繼承 BaseAdapter
class iTuneListViewAdapter : BaseAdapter(){

    var songs = listOf<SongItem>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    override fun getCount(): Int {
        return songs.size
    }

    override fun getItem(position: Int): Any {
        return songs.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflater = LayoutInflater.from(parent!!.context)
        val itemView = inflater.inflate(R.layout.itune_list_item,null)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(songs.get(position).cover)
        val textView = itemView.findViewById<TextView>(R.id.textView)
        textView.text = songs.get(position).title

        return itemView
    }

}