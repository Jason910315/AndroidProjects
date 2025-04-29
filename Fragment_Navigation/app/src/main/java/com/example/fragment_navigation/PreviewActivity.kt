package com.example.fragment_navigation

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.example.fragment_navigation.databinding.ActivityPreviewBinding

// 從 MainActvity 跳轉到此頁面
// MediaCOntroller: Android 提供的 UI 控制元件，能夠控制影片或音樂播放，例如播放/暫停按鈕、時間軸。
class PreviewActivity : AppCompatActivity(), MediaController.MediaPlayerControl{
    var title : String? = null
    var cover : Bitmap? = null
    var url : String? = null
    private var musicPlaying = false
    private var bufferPecentage = 0
    // MediaPlayer 是「播放引擎」，負責實際播放、控制進度、處理資源等低階操作。
    private var mediaPlayer = MediaPlayer()
    // 初始化 MediaController 物件， 是用來顯示播放控制 UI（播放/暫停/時間軸等）的一個「視覺介面」元件，控制 MediaPlayer 的播放。
    private  val mediaController by lazy{
        // 建立一個匿名類別，繼承 MediaController
        object : MediaController(this){
            override  fun show(timeout : Int){
                super.show(0) // 永遠顯示播放控制器
            }
            // 攔截並處理鍵盤事件
            override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
                // 當使用者按下「返回鍵」，呼叫 Activity 的返回方法，模擬使用者按下手機返回鍵，結束當前 Activity。
                if(event!!.keyCode == KeyEvent.KEYCODE_BACK){
                    onBackPressed()
                }
                return super.dispatchKeyEvent(event)
            }
        }
    }

    data class ViewModel(var title : String,var cover : Bitmap?)
    // ObservableField(...)，當資料改變時，會自動通知綁定的 UI 做更新
    val viewModel = ObservableField(ViewModel("",null))
    // 將 activity_preview.xml 綁訂到 binding，供 PreviewActivity 操作 layout 的元件
    val binding : ActivityPreviewBinding by lazy{
        DataBindingUtil.setContentView(this,R.layout.activity_preview)
    }
    // 螢幕旋轉或 Configuration 改變時，會 reCreate，需保存狀態
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 儲存目前音樂播放狀態與播放位置
        outState.putBoolean("musicPlaying",musicPlaying)
        outState.putInt("currentPosition",mediaPlayer.currentPosition)
    }
    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle:","Preview_onStart")
        // 在 Start 狀態，if musicPlaying is true，則 start()
        if(musicPlaying) mediaPlayer.start()
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle:","Preview_onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle:","Preview_onPause")
        // Activity 要進入 Background，若在播放中，就先暫停。
        if(musicPlaying) mediaPlayer.pause()
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle:","Preview_onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle:","Preview_onDestroy")
        // 釋放資源，隱藏撥放器
        mediaPlayer.release()
        mediaController.hide()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle:","Preview_onCreate")
        enableEdgeToEdge()
        // 使用 activity_preview 這個 layout
        setContentView(R.layout.activity_preview)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 接收從上個頁面傳送過來的資料，注意參數名稱要與上個頁面設定一致
        title = intent.getStringExtra("title")
        // cover 是 Bitmap，透過 Parcelable 傳遞
        cover = intent.getParcelableExtra("cover")
        url = intent.getStringExtra("url")
        // 用 binding 方式指派資料
        binding.title = title
        binding.cover = cover

        try{
            mediaPlayer.setDataSource(url)
            // 準備撥放
            mediaPlayer.setOnPreparedListener {
                Log.i("PreviewActivity","MediaPlayer is ready...")
                mediaPlayer.setOnCompletionListener {
                    musicPlaying = false
                    mediaController.show()
                }
                // 若之前有被暫停過或 Activity 有進到背景，則 savedInstanceState 會儲存先前狀態
                if(savedInstanceState != null){
                    musicPlaying = savedInstanceState.getBoolean("musicPlaying")
                    val position = savedInstanceState.getInt("currentPosition")
                    mediaPlayer.seekTo(position)
                    // 若在播放中，則呼叫 start()
                    if(musicPlaying) mediaPlayer.start()
                }
                // 控制 mediaController 要附著在哪個 View 上顯示
                mediaController.setAnchorView(binding.anchorView)
                mediaController.setMediaPlayer(this)
                // 預設：控制器會在一段時間後自動隱藏，但先前有覆寫 show() 讓她永遠顯示。
                mediaController.show()
            }
            mediaPlayer.setOnBufferingUpdateListener{
                    mediaPlayer,i -> bufferPecentage = i
            }
            mediaPlayer.prepareAsync()
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }
    fun onPreviewClick(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    // MediaPlayerControl 實作
    override fun start() {
        mediaPlayer.start()
        musicPlaying = true
    }

    override fun pause() {
        mediaPlayer.pause()
        musicPlaying = false
    }

    override fun getDuration(): Int {
        return mediaPlayer.duration
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun seekTo(pos: Int) {
        mediaPlayer.seekTo(pos)
    }

    override fun isPlaying(): Boolean {
        return musicPlaying
    }

    override fun getBufferPercentage(): Int {
        return bufferPecentage
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getAudioSessionId(): Int {
        return mediaPlayer.audioSessionId
    }
}