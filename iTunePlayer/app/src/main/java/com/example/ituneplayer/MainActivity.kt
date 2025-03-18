package com.example.ituneplayer

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 讓 parserURL() 函式在背景執行，切換到 IO thread 執行
        GlobalScope.launch(Dispatchers.Main){
            // 宣告 songs 為一個 SongItem 物件
            var songs = listOf<SongItem>()

            // 開啟 IO 背景執行
            withContext(Dispatchers.IO){
                // 執行 parseURL 函式爬取網站資料並存入 songs 物件
                songs = iTuneXMLParser().parseURL("https://itunes.apple.com/us/rss/topsongs/limit=25/xml")
            }
            val linearLayout = findViewById<LinearLayout>(R.id.main)
            for(song in songs){
                val textView = TextView(application)
                textView.text = song.title
                linearLayout.addView(textView)
            }
        }
    }

}