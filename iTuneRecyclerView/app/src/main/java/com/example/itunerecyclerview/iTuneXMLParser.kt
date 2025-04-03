package com.example.itunerecyclerview

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
                            songList.add(SongItem(title , cover))
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