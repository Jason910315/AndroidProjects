package com.example.fragment_navigation

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.navigation.fragment.navArgs
import com.example.fragment_navigation.databinding.ActivityPreviewBinding
import com.example.fragment_navigation.databinding.FragmentItunePreviewBinding



class iTunePreviewFragment : Fragment(), MediaController.MediaPlayerControl  {
    private var title: String? = null
    private var cover: Bitmap? = null
    private var url: String? = null
    private var musicPlaying = false
    private var bufferPercentage = 0

    // args 是 iTunePreviewFragmentArgs 類別的實例，用來取得從前一個 Fragment 傳來的參數
    // by navArgs() 會讓系統自動從 Bundle 中解析出 title、cover、url
    // 這行會抓到你在 iTuneListFragment 使用的這句話所帶來的參數：
    val args : iTunePreviewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // parentFragment ≠ null 時執行
        parentFragment?.let{
            // 顯示此首歌曲的名稱、圖示、url
            previewSong(args.title,args.cover,args.videoUrl,0)
        }
    }

    override fun onPause() {
        super.onPause()
        // Fragment 要進入 Background，若在播放中，就先暫停。
        if (musicPlaying) mediaPlayer.pause()
    }

    override fun onStart() {
        super.onStart()
        if (musicPlaying) mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mediaController.hide()
    }

    private val mediaPlayer = MediaPlayer()
    private val mediaController by lazy {
        object : MediaController(activity) {
            override fun show(timeout: Int) {
                super.show(0) // 永久顯示 MediaController
            }
            // 當使用者按下「返回鍵」，呼叫 Activity 的返回方法，模擬使用者按下手機返回鍵，結束當前 Activity。
            override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
                if (event!!.keyCode == KeyEvent.KEYCODE_BACK) {
                    activity?.onBackPressed()
                }
                return super.dispatchKeyEvent(event)
            }
        }
    }
    // 由 fragment_itune_preview.xml 自動產生的 Binding 類別，稍後會初始化 binding 變數
    lateinit var binding: FragmentItunePreviewBinding
    // 覆寫 onCreateView，用 DataBinding 載入 fragment_itune_preview 的 layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 綁定 fragment_itune_preview 這個 layout 的變數
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_itune_preview, container, false)
        return binding.root
    }


    fun onPreviewClick(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    // MediaController 實作
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

    override fun seekTo(p0: Int) {
        mediaPlayer.seekTo(p0)
    }

    override fun isPlaying(): Boolean {
        return musicPlaying
    }

    override fun getBufferPercentage(): Int {
        return bufferPercentage
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
    fun previewSong(songTitle: String, songCover: Bitmap,videoUrl : String, position: Int) {
        title = songTitle
        cover = songCover
        url = videoUrl
        binding.title = songTitle
        binding.cover = songCover

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.setOnPreparedListener {
                Log.i("PreviewActivity", "MediaPlayer is read....")
                mediaPlayer.setOnCompletionListener {
                    musicPlaying = false
                    mediaController.show() // force to show the play button
                }
                mediaController.setAnchorView(binding.anchorView)
                mediaController.setMediaPlayer(this)
                mediaController.show()
                if (position > 0) {
                    mediaPlayer.seekTo(position)
                }
                if (musicPlaying) {
                    mediaPlayer.start()
                }
            }
            mediaPlayer.setOnBufferingUpdateListener { mediaPlayer, i ->
                bufferPercentage = i
            }
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}