package com.example.ituneplayer

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class iTuneXMLParser {
    // 建立一個 XmlPullParserFactory 的實例，它負責決定並提供適當的 XML 解析器
    val factory = XmlPullParserFactory.newInstance()
    // 請求工廠創建一個新的 XML 解析器 (XmlPullParser) 實例
    val parser = factory.newPullParser()
    // suspend 只能在背景使用，或是在 Coroutine 使用
    suspend fun parseURL(url : String) : List<SongItem>{
        val songList = mutableListOf<SongItem>()
        var title = ""
        // Parse XML
        try{
            // openStream 是一個網路的 operation，Android 不允許其在前端做，需在 Coroutine 做(背景)
            // 使用 openStream() 方法開啟與該 URL 的連線，並獲取資料流（InputStream）
            val inputStream = URL(url).openStream()
            parser.setInput(inputStream,null)

            // The next() reads the current event/element and move the cursor pointer to the next event

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
                    }
                    // 當讀到結尾 tag 時
                    XmlPullParser.END_TAG -> {
                        // 讀到 entry 代表為一首歌的資料結尾
                        if(parser.name == "entry"){
                            songList.add(SongItem(title))
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