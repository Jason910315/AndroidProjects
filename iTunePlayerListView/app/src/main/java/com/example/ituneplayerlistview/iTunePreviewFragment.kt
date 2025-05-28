package com.example.ituneplayerlistview

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
import com.example.ituneplayerlistview.databinding.ActivityPreviewBinding
import com.example.ituneplayerlistview.databinding.FragmentItunePreviewBinding



class iTunePreviewFragment : Fragment(), MediaController.MediaPlayerControl  {
    private var title: String? = null
    private var cover: Bitmap? = null
    private var url: String? = null
    private var musicPlaying = false
    private var bufferPercentage = 0

    val args : iTunePreviewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragment?.let{
            previewSong(args.title,args.cover,args.url,0)
        }
    }

    override fun onPause() {
        super.onPause()
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
                super.show(0)
            }
            override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
                if (event!!.keyCode == KeyEvent.KEYCODE_BACK) {
                    activity?.onBackPressed()
                }
                return super.dispatchKeyEvent(event)
            }
        }
    }

    lateinit var binding: FragmentItunePreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_itune_preview, container, false)
        return binding.root
    }


    fun onPreviewClick(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

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
    fun previewSong(songTitle: String, songCover: Bitmap, songUrl: String, position: Int) {
        title = songTitle
        cover = songCover
        url = songUrl
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