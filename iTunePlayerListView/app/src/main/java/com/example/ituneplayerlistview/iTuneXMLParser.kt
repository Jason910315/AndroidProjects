package com.example.ituneplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.ituneplayerlistview.SongItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class iTuneXMLParser {
    // 建立一個 XmlPullParserFactory 的實例，它負責決定並提供適當的 XML 解析器
    val factory = XmlPullParserFactory.newInstance()
    // 請求工廠創建一個新的 XML 解析器 (XmlPullParser) 實例，他不會一次讀完整份文件，而是一個一個事件讀來
    val parser = factory.newPullParser()
    // suspend 只能在背景使用，或是在 Coroutine 使用，因為其需要網路請求
    suspend fun parseURL(url : String) : List<SongItem>{
        val songList = mutableListOf<SongItem>()
        var title = ""
        // 建立一個 Bitmap 物件
        var cover : Bitmap? = null
        var m4aurl = ""
        // Parse XML
        try{
            // openStream 是一個網路的 operation，Android 不允許其在前端做，需在 Coroutine 做(背景)
            // 使用 openStream() 方法開啟與該 URL 的連線，並獲取資料流（InputStream）
            val inputStream = URL(url).openStream()
            parser.setInput(inputStream,null)

            // The next() reads the current event/element and move the cursor pointer to the next event
            // parser.next()：每次讀進一個「事件」，像是開始標籤（START_TAG）、結束標籤（END_TAG）、文字內容（TEXT）等等
            var eventType = parser.next()
            // 當不是讀到檔案結尾就持續迴圈
            while(eventType != XmlPullParser.END_DOCUMENT){
                when(eventType){
                    // 若讀到各種起始 tag
                    XmlPullParser.START_TAG -> {
                        // 歌曲名稱的 tag 是位於 <feed> -> <entry> -> <title>，深度為 3
                        if(parser.name == "title" && parser.depth == 3){
                            // 若目前為 START_TAG，則返回其內容
                            title = parser.nextText()
                            Log.i("title:",title)
                        }
                        else if(parser.name == "link"){
                            if(parser.getAttributeValue(null,"type") == "audio/x-m4a"){
                                m4aurl = parser.getAttributeValue(null,"href")
                                Log.i("m4aUrl",m4aurl)
                            }
                        }
                        else if(parser.name == "im:image"){
                            if(parser.getAttributeValue(null,"height") == "170"){
                                val coverUrl = parser.nextText()
                                Log.i("coverUrl:",coverUrl)
                                val inputStream = URL(coverUrl).openStream()
                                cover = BitmapFactory.decodeStream(inputStream)
                            }
                        }
                    }
                    // 當讀到結尾 tag 時
                    XmlPullParser.END_TAG -> {
                        // 讀到 entry 代表為一首歌的資料結尾
                        if(parser.name == "entry"){
                            songList.add(SongItem(title , cover, m4aurl))
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        catch (e : Throwable){
            e.printStackTrace()
        }
        Log.i("list:",songList.toString())
        return songList
    }
}