package com.example.ituneplayerlistview

import android.app.ListActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.mutableIntListOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ituneplayer.SongItem
import com.example.ituneplayer.iTuneXMLParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ListActivity() {
    val titles = mutableListOf<String>()
    // 使用 Adapter 顯示 ListView
    val adapter by lazy{
        ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titles)
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        Log.i("Jason:","User clicked " + titles[position])
        Toast.makeText(this,titles[position],Toast.LENGTH_LONG).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listAdapter = adapter
        // 讓 parserURL() 函式在背景執行，切換到 IO thread 執行
        GlobalScope.launch(Dispatchers.Main){
            // 宣告 songs 為一個 SongItem 物件
            var songs = listOf<SongItem>()

            // 開啟 IO 背景執行
            withContext(Dispatchers.IO){
                // 執行 parseURL 函式爬取網站資料並存入 songs 物件
                songs = iTuneXMLParser().parseURL("https://itunes.apple.com/us/rss/topsongs/limit=25/xml")
            }
            for(song in songs){
                titles.add(song.title)
            }
            adapter.notifyDataSetChanged()
        }
    }
}