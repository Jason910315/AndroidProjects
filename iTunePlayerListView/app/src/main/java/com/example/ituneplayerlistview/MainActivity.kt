package com.example.ituneplayerlistview

import android.app.ListActivity
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.ituneplayerlistview.SongItem
import com.example.ituneplayerlistview.iTuneXMLParser
import com.example.ituneplayerlistview.databinding.SwiperefreshlayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val previewFragment = supportFragmentManager.findFragmentById(R.id.previewFragment) as iTunePreviewFragment?
        // 平板
        if(previewFragment != null){
            previewFragment.previewSong(title,cover,url,0)
        }
        /// 手機
        else{
            val action = iTuneListFragmentDirections.actionITuneListFragmentToITunePreviewFragment(title,cover,url)
            // 找到手機板 activity_main 的 id
            findNavController(R.id.navHostFragment).navigate(action)
        }
    }
}