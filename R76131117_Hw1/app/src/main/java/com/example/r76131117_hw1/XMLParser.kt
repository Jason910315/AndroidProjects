package com.example.r76131117_hw1

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
                        if(parser.name == "title" && parser.depth == 3){
                            title = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if(parser.name == "entry"){
                            videoList.add(VideoItem(title))
                        }
                    }
                }
                eventType = parser.next()
            }
        }
        catch (e : Throwable){
            e.printStackTrace()
        }
        return videoList
    }


}