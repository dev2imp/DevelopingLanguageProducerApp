package com.developeros.languageproducer.utils

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.Buffer
object GetLangsFromBlogSingleton {
    var LangArr = ArrayList<String>()
   suspend fun getLangs():ArrayList<String>{

        // TODO url ->
        var url ="https://practiselanguage.blogspot.com/2021/12/language.html"
        //By default we want to put english language
        LangArr.add("English")
       var allText=""
       try {
           var Url = URL(url)
           var httpURLConnection: HttpURLConnection = Url.openConnection() as HttpURLConnection
           var inputStream: InputStream =httpURLConnection.inputStream
           var reader = BufferedReader(InputStreamReader(inputStream))

           while(reader.readLine()!=null){
               allText = allText + reader.readLine()+ " "
           }
       }catch (e:Exception){
       }
       var doc: Document = Jsoup.parse(allText)
       var elements: Elements = doc.getElementsByTag("p")
       for(element in elements){
           var line = element.text()
           if(line.replace(" ","").length>0){
               LangArr.add(line)
           }
       }
        return LangArr
    }
}