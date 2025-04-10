package com.example.r76131117_hw2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
// 此 class 設計一個 RecyclerView 的 Adapter 供 MainActivity 使用

// 這個 adapter 繼承自 RecyclerView.Adapter<T>
class RecyclerViewAdapter(data : List<VideoItem>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    // 建利 video 變數，型別為 VideoItem 的 List，用來存放每筆新聞資料
    // 當 videos 被重新指定資料時，會更新內部變數 field，並呼叫 notifyDataSetChanged 通知畫面重新載入
    var videos : List<VideoItem> = data
        set(value){
            field = value
            notifyDataSetChanged()
        }
    // 可當作未來 ListView 每一行的元件參考，省略每行綁資料都要用一次 findViewById()
    class ViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val textView = view.findViewById<TextView>(R.id.textView)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
    }
    // 當 RecyclerView 需要建立一個新的行時(初次畫面或捲動出新的 item)才呼叫，不用每一行都重新 inflate 一次
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // 使用 itune_list_item.xml 樣板，並將其轉換成一個 View 物件，代表這一行的畫面長相
        val view = inflater.inflate(R.layout.itune_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    // 當 RectclerView 要顯示一行資料時會呼叫此函式，用於繫結資料
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = videos.get(position).title
        holder.imageView.setImageBitmap(videos.get(position).cover)
    }
}