package com.example.r76131117_hw2

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val  title = mutableListOf<String>()

    // 用 RecyclerView 當 Adapter
    val adapter by lazy{
        RecyclerViewAdapter(listOf<VideoItem>())
    }
    val swipeRefreshLayout by lazy{
        findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    }
    fun loadlist(){
        GlobalScope.launch(Dispatchers.Main) {
            var videos_list = listOf<VideoItem>()
            withContext(Dispatchers.IO){
                videos_list = XMLParser().parserURL("https://www.youtube.com/feeds/videos.xml?channel_id=UCupvZG-5ko_eiXAupbDfxWw")
            }
            // 將爬取到的 videos_list 指派給 adapter 裡定義的 videos
            adapter.videos = videos_list
            adapter.notifyDataSetChanged()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        // 使用 swipeRefreshLayout 做為主頁面樣板
        setContentView(R.layout.swiperefreshlayout)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                swipeRefreshLayout.isRefreshing = true
                // 刷新頁面時(swipe)，需清空 adapter 裡的資料，重新指派資料
                adapter.videos = listOf<VideoItem>()
                loadlist()
                Log.i("refresh:","true")
                swipeRefreshLayout.isRefreshing = false
            }
        })
        loadlist()
    }
}