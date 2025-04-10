package com.example.itunerecyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
// 此 class 設計一個 ListView 的 Adapter 供 MainActivity 使用

// 要使用 Adapter 物件要繼承 BaseAdapter
class iTuneListViewAdapter() : BaseAdapter(){
    // 宣告一個 SongItem 型別的 List 物件
    var songs = listOf<SongItem>()
        // 當 songs 被重新指定新資料時，會更新內部變數 field，然後呼叫 notifyDataSetChanged() 通知畫面重新載入
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return songs.size
    }

    override fun getItem(position: Int): Any {
        // 傳回第 position 筆的 SongItem 物件
        return songs.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // 產生與顯示每一列的 ListView 內容，ListView 會自動呼叫，畫出第 position 筆資料的 View
    // ListView 每一列都要重新 inflate 將樣板轉成 View 物件，並重新 findViewById 來顯示內容，耗效能
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // 建立一個 LayoutInflater，用來把 XML layout 轉成實際的 View。
        // context: 代表目前程式所處的環境（Activity、App等）
        var inflater = LayoutInflater.from(parent!!.context)
        // 將 itune_list_item.xml 轉換成一個 View 物件，代表這一行的畫面長相
        // 代表 iTuneListViewAdapter 會使用 itune_list_item 這個樣板來顯示每一列的 View
        val itemView = inflater.inflate(R.layout.itune_list_item,null)

        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(songs.get(position).cover)
        val textView = itemView.findViewById<TextView>(R.id.textView)
        textView.text = songs.get(position).title

        return itemView
    }

}