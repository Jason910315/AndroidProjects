package com.example.databinding_activitylife

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.databinding_activitylife.databinding.SwiperefreshlayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 現在使用 RecyclerView，原先使用 ListView
class MainActivity : AppCompatActivity(),iTuneRecyclerViewAdapter.RecyclerViewClickListener {
    val titles = mutableListOf<String>()

    val adapter by lazy {
        // 使用 ListViewAdapter
        // iTuneListViewAdapter()
        // 改使用 RecyclerViewAdapter
        iTuneRecyclerViewAdapter(listOf<SongItem>(),this)
    }
    val swipeRefreshLayout by lazy {
        findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    }
    // 對應到 swiperefreshlayout.xml 的 DataBinding 物件
    val binding : SwiperefreshlayoutBinding by lazy{
        // 回傳一個自動產生的 binding 實例，並綁訂到 swiperefreshlayout
        DataBindingUtil.setContentView(this ,R.layout.swiperefreshlayout)
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

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle:","Main_onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle:","Main_onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle:","Main_onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle:","Main_onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle:","Main_onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("LifeCycle:","Main_onRestart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle:","Main_onCreate")
        setContentView(R.layout.swiperefreshlayout)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        // val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // 以 DataBinding 的方式取代 findViewById，存取 XML layout 中的 RecyclerView 元件

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener(object : OnRefreshListener {
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
    override fun onClick(position: Int) {
        Log.i("click:",adapter.songs.get(position).title)
        Toast.makeText(this,adapter.songs.get(position).title, Toast.LENGTH_LONG).show()
        // 建立一個 intent，表示要從現在的頁面(MainActivity)跳轉到下個頁面(PreviewActivity)
        // 使用顯式 Intent，明確指名要跳轉的 Activity
        val intent = Intent(this,PreviewActivity::class.java)
        val song = adapter.songs.get(position)
        // 使用 Intent.putExtra(key, value) 傳送資料給 PreviewActivity
        intent.putExtra("title",song.title)
        intent.putExtra("cover",song.cover)
        intent.putExtra("url",song.url)
        // 執行跳轉頁面
        startActivity(intent)
    }
}