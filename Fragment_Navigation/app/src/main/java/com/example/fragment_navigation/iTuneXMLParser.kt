package com.example.fragment_navigation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.fragment_navigation.SongItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class iTuneXMLParser {
    val factory = XmlPullParserFactory.newInstance()
    val parser = factory.newPullParser()

    suspend fun parserURL(url : String) : List<VideoItem>{
        val videoList = mutableListOf<VideoItem>()
        var title = ""
        var videoID = ""
        var cover : Bitmap?=null
        var videoUrl = ""
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
                        if (parser.name == "link") {
                            val rel = parser.getAttributeValue(null, "rel")
                            if (rel == "alternate") {
                                videoUrl = parser.getAttributeValue(null, "href")
                                Log.i("YouTube Link:", videoUrl)
                                // 這裡你就可以把 videoUrl 存到你要的變數中（例如 model.link）
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if(parser.name == "entry"){
                            // 將爬取到的 title、cover 加進 videoList 並 return
                            videoList.add(VideoItem(title,cover,videoUrl))
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        catch (e : Throwable){
            Log.e("XML", "Parsing error", e)
            e.printStackTrace()
            val test = URL("https://www.google.com").readText()
            Log.i("NetworkTest", test)
        }
        Log.i("list:",videoList.toString())
        return videoList
    }
}