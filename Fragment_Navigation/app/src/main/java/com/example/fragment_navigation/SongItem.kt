package com.example.fragment_navigation
import android.graphics.Bitmap

// 存資料的 class，class 的建構子參數必須加上 val/var
// Primary constructor
data class SongItem(val title : String = "", val cover : Bitmap? = null,val url : String = ""){

}
