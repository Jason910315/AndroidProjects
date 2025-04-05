package com.example.itunerecyclerview

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
import org.w3c.dom.Text
// 從 MainActvity 跳轉到此頁面
class PreviewActivity : AppCompatActivity() {
    var title : String? = null
    var cover : Bitmap? = null
    var url : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        // 注意此處找的是 activity_preview 這個 layout 裡的元素
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(cover)
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = title
        val playButton = findViewById<Button>(R.id.playerButton)
        // 設定 Play 按鈕被點擊的動作
        playButton.setOnClickListener{
            // 建立一個「開啟網址動作」的隱式 Intent，並將音樂連結 url 轉成 Uri
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.putExtra("url",url)
            startActivity(intent)
        }
    }
}