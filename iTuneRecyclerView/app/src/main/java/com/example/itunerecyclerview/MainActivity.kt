package com.example.itunerecyclerview

import android.app.ListActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 現在使用 RecyclerView，原先使用 ListView
class MainActivity : AppCompatActivity() {
    val titles = mutableListOf<String>()

    val adapter by lazy {
        // 使用 ListViewAdapter
        // iTuneListViewAdapter()
        // 改使用 RecyclerViewAdapter
        iTuneRecyclerViewAdapter(listOf<SongItem>())
    }
    val swipeRefreshLayout by lazy {
        findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipeRefreshLayout)
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
        // listAdapter = adapter  // 此處使用 ListActivity 需要指派
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
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