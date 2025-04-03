package com.example.itunerecyclerview

import android.app.ListActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ListActivity() {
    val titles = mutableListOf<String>()

    val adapter by lazy {
        iTuneListViewAdapter()
    }
    val swipeRefreshLayout by lazy {
        findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    }
    // 覆寫 onListItemClick 函式，製作 ListItem 被點擊後的事件
    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        // 呼叫父類別的原本邏輯，l: 被點擊的 listView，v: 被點擊的那一列，position: 被點擊項目的索引
        super.onListItemClick(l, v, position, id)
        val title = adapter.songs[position].title
        Log.i("Jason:","User clicked:" + title)
        // 顯示一個短暫的提示訊息（Toast）在畫面上，Toast.LENGTH_LONG: 持續的時間，大約3.5秒
        val toast = Toast.makeText(this,title, Toast.LENGTH_LONG)
        toast.show()
    }
    fun loadlist(){

        GlobalScope.launch(Dispatchers.Main) {
            var songs = listOf<SongItem>()
            withContext(Dispatchers.IO){
                songs = iTuneXMLParser().parseURL("https://itunes.apple.com/us/rss/topsongs/limit=25/xml")
            }
            // 將剛剛爬取到的 songs 指派給 adapter 裡的 songs 變數
            adapter.songs = songs
            adapter.notifyDataSetChanged()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.swiperefreshlayout)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        listAdapter = adapter
        swipeRefreshLayout.setOnRefreshListener(object : OnRefreshListener{
            override fun onRefresh() {
                swipeRefreshLayout.isRefreshing = true  // 啟動刷新動畫，轉圈畫面
                // 在刷新（Swipe Refresh）時，把目前 adapter 裡的資料清空，準備放新資料
                adapter.songs = listOf<SongItem>()
                loadlist()                              // 執行 loadlist 抓取資料
                Log.i("refresh","true")
                swipeRefreshLayout.isRefreshing = false // 結束刷新動畫
            }
        })
        loadlist()
    }
}