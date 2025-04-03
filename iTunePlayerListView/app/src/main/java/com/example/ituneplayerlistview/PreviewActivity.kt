package com.example.ituneplayerlistview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PreviewActivity : AppCompatActivity() {
    var title : String?= null
    var cover : Bitmap?= null
    var url : String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preview)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title = intent.getStringExtra("title")
        cover = intent.getParcelableExtra("cover")
        url = intent.getStringExtra("url")

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(cover)
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = title

        val playButton = findViewById<Button>(R.id.playerButton)
        playButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.putExtra("url",url)
            startActivity(intent)
        }
    }
}