package com.example.r76131117_hw2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class XMLParser {
    val factory = XmlPullParserFactory.newInstance()
    val parser = factory.newPullParser()

    suspend fun parserURL(url : String) : List<VideoItem>{
        val videoList = mutableListOf<VideoItem>()
        var title = ""
        var videoID = ""
        var cover : Bitmap?=null
        try{
            val inputStream = URL(url).openStream()
            parser.setInput(inputStream,null)

            var eventType = parser.next()
            while(eventType != XmlPullParser.END_DOCUMENT){
                when(eventType){
                    XmlPullParser.START_TAG -> {
                        // 當爬取到 yt:videoId 的 tag 時
                        if(parser.name == "yt:videoId" && parser.depth == 3){
                            videoID = parser.nextText()
                            // 印出該部影片的 videoID
                            Log.i("videoID:",videoID)
                        }
                        // 當爬取到 title
                        if(parser.name == "title" && parser.depth == 3){
                            title = parser.nextText()
                            Log.i("title:",title)
                        }
                        // 當爬取到 media:thumbnail，代表此新聞的圖片
                        if(parser.name == "media:thumbnail"){
                            // 取得此標籤的 url 屬性值
                            val coverUrl = parser.getAttributeValue(null,"url")
                            Log.i("coverUrl:",coverUrl)
                            val inputStream = URL(coverUrl).openStream()
                            cover = BitmapFactory.decodeStream(inputStream)
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if(parser.name == "entry"){
                            // 將爬取到的 title、cover 加進 videoList 並 return
                            videoList.add(VideoItem(title,cover))
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        catch (e : Throwable){
            e.printStackTrace()
        }
        Log.i("list:",videoList.toString())
        return videoList
    }
}