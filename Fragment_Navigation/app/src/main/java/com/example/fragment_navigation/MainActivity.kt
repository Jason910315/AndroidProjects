package com.example.fragment_navigation

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// 現在使用 RecyclerView，原先使用 ListView
class MainActivity : AppCompatActivity(),iTuneListFragment.OnSongSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle:","Main_onCreate")
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        // val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // 以 DataBinding 的方式取代 findViewById，存取 XML layout 中的 RecyclerView 元件


    }

    override fun onSongSelected(title: String, cover: Bitmap, url: String) {
        Log.i("MainActivity","onSOngSelected() is called")
        val previewFragment = supportFragmentManager.findFragmentById(R.id.previewFragment) as iTunePreviewFragment
        if(previewFragment != null){
            previewFragment.previewSong(title,cover,url,0)
        }
    }
}