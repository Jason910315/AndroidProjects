package com.example.ituneplayerlistview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.ituneplayerlistview.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {
    var title : String?= null
    var cover : Bitmap?= null
    var url : String?= null
    val binding : ActivityPreviewBinding by lazy{
        // 新增
        DataBindingUtil.setContentView<ActivityPreviewBinding>(this,R.layout.activity_preview)
    }

    override fun onPause() {
        super.onPause()
        Log.i("LifeCycle:","onPause()")
        if(musicPlaying) mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        Log.i("LifeCycle:","onResume()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("musicPlaying",musicPlaying)
        outState.putInt("currentPosition",musicPlayer)
    }

    override fun onStart() {
        super.onStart()
        Log.i("LifeCycle:","onStart()")
        if(musicPlaying) mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("LifeCycle:","onDestroy()")
        mediaPlayer.release()
        mediaController.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("LifeCycle:","onCreate()")
        enableEdgeToEdge()
//        setContentView(R.layout.activity_preview)


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        title = intent.getStringExtra("title")
        cover = intent.getParcelableExtra("cover")
        url = intent.getStringExtra("url")

        binding.title = title
        binding.cover = cover
        binding.playerButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.putExtra("url",url)
            startActivity(intent)
        }
//        val imageView = findViewById<ImageView>(R.id.imageView)
//        imageView.setImageBitmap(cover)
//        val textView = findViewById<TextView>(R.id.textView)
//        textView.text = title
//
//        val playButton = findViewById<Button>(R.id.playerButton)
//        playButton.setOnClickListener{
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            intent.putExtra("url",url)
//            startActivity(intent)
//        }
    }
}