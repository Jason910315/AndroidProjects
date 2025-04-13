package com.example.databinding_activitylife

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class iTuneXMLParser {
    val factory = XmlPullParserFactory.newInstance()
    val parser = factory.newPullParser()

    suspend fun parseURL(url : String) : List<SongItem>{

        val songList = mutableListOf<SongItem>()
        var title = ""
        var cover : Bitmap? = null
        var m4aurl = ""
        try{
            val inputStream = URL(url).openStream()
            parser.setInput(inputStream,null)
            var eventType = parser.next()
            while(eventType != XmlPullParser.END_DOCUMENT){
                when(eventType){
                    XmlPullParser.START_TAG -> {
                        if(parser.name == "title" && parser.depth == 3){
                            title = parser.nextText()
                            Log.i("title:",title)
                        }
                        else if(parser.name == "link"){
                            // 取得這個 <link> 標籤的 type 屬性值，若 type 屬性 = "audio/x-m4a
                            if(parser.getAttributeValue(null,"type") == "audio/x-m4a"){
                                // 取得這個 <link> 標籤的 href 屬性值
                                m4aurl = parser.getAttributeValue(null,"href")
                                Log.i("m4aurl",m4aurl)
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
                    XmlPullParser.END_TAG -> {
                        if(parser.name == "entry"){
                            // 將取出的三個屬性值包裝成 SongItem 物件並加入 songList
                            songList.add(SongItem(title , cover, m4aurl))
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
        Log.i("list:",songList.toString())
        return songList
    }
}