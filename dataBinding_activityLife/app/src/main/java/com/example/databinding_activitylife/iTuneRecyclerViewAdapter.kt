package com.example.databinding_activitylife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding_activitylife.databinding.ItuneListItemBinding

// 此 class 設計一個 RecyclerView 的 Adapter 供 MainActivity 使用

// 這個 adapter 繼承自 RecyclerView.Adapter<T>
class iTuneRecyclerViewAdapter(data : List<SongItem>,val listener : RecyclerViewClickListener?=null) :
    RecyclerView.Adapter<iTuneRecyclerViewAdapter.ViewHolder>(){
    // 外部的 Activity（或 Fragment）只要實作這個介面，就能接收到點擊通知
    // Adapter 不需要知道點擊後做什麼，把這件事交給外部 Activity 控制
    interface  RecyclerViewClickListener{
        fun onClick(position : Int)
    }
    // 當 song 被重新指定新資料時，會更新內部變數 field，然後呼叫 notifyDataSetChanged() 通知畫面重新載入
    var songs : List<SongItem> = data
        set(value){
            field = value
            notifyDataSetChanged()
        }
    // 建立 ViewHolder 類別 讓其他函式使用
    // 綁定到 itune_list_item.xml 的 layout。並回傳 View 的 root 供畫面操作裡面的元件。
    class ViewHolder(val binding : ItuneListItemBinding) : RecyclerView.ViewHolder(binding.root){
//        val textView = view.findViewById<TextView>(R.id.textView)
//        val imageView = view.findViewById<ImageView>(R.id.imageView)
    }
    // 如何建立每一行的 View
    // 其只有在系統需要一個新的 View 時才呼叫，例如當 RecyclerView 需要建立一個新的行時(初次畫面或捲動出新的 item)，而不像 ListView 在每一列都要重新 inflate
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // parent: 是 RecyclerView 本體，表示這個 view 會被加進哪個容器; false: 表示先不加進 RecyclerView，等系統幫你處理
        val binding = ItuneListItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }
    // 每當 RecyclerView 準備要顯示一行資料時，會呼叫這個方法，如何將資料繫結到每行
    override fun onBindViewHolder(holder: iTuneRecyclerViewAdapter.ViewHolder, position: Int) {
        // onCreateViewHolder 建立的 binding 來做資料繫結
        // songs 型別為 SongItem(val title : String = "", val cover : Bitmap? = null,val url : String = "")，繫結到整筆 songs 資料
        holder.binding.songs = songs.get(position)
        holder.binding.root.setOnClickListener {
            listener?.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}