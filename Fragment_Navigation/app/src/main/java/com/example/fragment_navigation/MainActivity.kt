package com.example.fragment_navigation

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController

// 現在使用 RecyclerView，原先使用 ListView
class MainActivity : AppCompatActivity(),iTuneListFragment.OnSongSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle:","Main_onCreate")
        // 主畫面 layout 是 activity_main，且 activity_main 裡又使用了兩個 fragment 呈現分割畫面
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
    // 當 iTuneListFragment 中的歌曲被點擊時，呼叫以下函式
    override fun onSongSelected(title: String, cover: Bitmap,videoUrl : String) {
        Log.i("MainActivity","onSOngSelected() is called")
        // 利用 supportFragmentManager 取得目前畫面上的 iTunePreviewFragment 實例
        // previewFragment 定義在 activity.xml 裡
        val previewFragment = supportFragmentManager.findFragmentById(R.id.previewFragment) as iTunePreviewFragment?
        // 若有找到 previewFragment 則呼叫 previewSong()，在 iTunePreviewFragment 實作

        // 平板畫面，因為一定可以找到 previewFragment，其與 listfragment 在同一畫面
        if(previewFragment != null){
            previewFragment.previewSong(title,cover,videoUrl,0)
        }
        // 手機畫面，找不到 previewFragment，代表要用 navigation 導航
        else{
            // iTuneListFragmentDirections 是 SafeArgs 為 iTuneListFragment 自動生成的類別，提供所有可從此 Fragment 發出的導航動作。
            // 傳遞參數 title,cover,url 到 iTunePreviewFragment 裡
            val action = iTuneListFragmentDirections.actionITuneListFragmentToITunePreviewFragment(title,cover,videoUrl)
            // NavController 負責 Fragment 之間的導航，導航到其他 Fragment
            findNavController(R.id.navHostFragment).navigate(action)
        }
    }


}