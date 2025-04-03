package com.example.ituneplayerlistviewdemo

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.ituneplayerlistviewdemo.R
import com.example.ituneplayerlistviewdemo.SongItem
import com.example.ituneplayerlistviewdemo.iTuneXMLParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ListActivity(){
    val titles = mutableListOf<String>()
    // Adapter: 把資料（像 List、Array、Cursor 等）轉換成可以顯示在畫面上的 View 元件（像 ListView、RecyclerView）
    // 此處 Adapter 是負責將 titles 的資料轉成 ListView 中的每一列(使用 simple_list_item_1 樣板)
    // ArrayAdapter 把 titles: MutableList<String> 轉成每一列的文字
    // by lazy: 延遲初始化，變數只有在第一次被使用時才會被建立出來，節省資源
    val adapter by lazy{
        ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titles)
    }
    val swipeRefreshLayout by lazy{
        findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    }

    // 覆寫 onListItemClick 函式，製作 ListItem 被點擊後的事件
    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        // 呼叫父類別的原本邏輯，l: 被點擊的 listView，v: 被點擊的那一列，position: 被點擊項目的索引
        super.onListItemClick(l, v, position, id)
        Log.i("Jason:","User clicked:" + titles[position])
        // 顯示一個短暫的提示訊息（Toast）在畫面上，Toast.LENGTH_LONG: 持續的時間，大約3.5秒
        val toast = Toast.makeText(this,titles[position],Toast.LENGTH_LONG)
        toast.show()
    }
    // 重整頁面的函式，也等於原先爬取 XML 並顯示在 listView
    fun loadlist(){
        // 讓 parserURL() 函式在背景執行，切換到 IO thread 執行
        // 注意 Dispatchers.Main 代表這段協程是在「主執行緒」啟動的（也就是可以操作 UI）
        GlobalScope.launch(Dispatchers.Main){
            // 宣告 songs 為一個 SongItem 物件
            var songs = listOf<SongItem>()

            // 切換到 IO 背景執行，避免阻塞 UI
            withContext(Dispatchers.IO){
                // 執行 parseURL 函式爬取網站資料並存入 songs 物件
                songs = iTuneXMLParser().parseURL("https://itunes.apple.com/us/rss/topsongs/limit=25/xml")
            }
            for(song in songs){
               // tiles 是綁定到 adapter 上，故會即時顯示在 ListView 上
                titles.add(song.title)
            }
            // 告訴 Adapter 資料已經更新了，請重畫畫面，因為 UI 本身是不會自動知道你改了資料的
            adapter.notifyDataSetChanged()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        // 設定畫面使用 swiperefreshlayout.xml 作為版面
        setContentView(R.layout.swiperefreshlayout)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        // 將先前建立的 adapter 設定給 ListView，listAdapter 是 ListView 的屬性，用來顯示資料
        // 找到畫面上的 SwipeRefreshLayout，用來實作下拉更新功能

        // 準備好的 adapter，綁定到 ListActivity 內建的 ListView 來顯示資料
        listAdapter = adapter

        swipeRefreshLayout.setOnRefreshListener(object : OnRefreshListener{
            override fun onRefresh() {
                swipeRefreshLayout.isRefreshing = true  // 啟動刷新動畫，轉圈畫面
                titles.clear()
                adapter.notifyDataSetChanged()
                loadlist()                              // 執行 loadlist 抓取資料
                Log.i("refresh","true")
                swipeRefreshLayout.isRefreshing = false // 結束刷新動畫
            }
        })
        loadlist()   // 啟動 Activity 時就呼叫 loadlist()，抓取第一次資料
    }
}