package com.developeros.languageproducer.utils

import android.util.Log
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel
import java.io.BufferedReader
import java.io.StringReader
import java.lang.NullPointerException
import java.util.regex.Pattern

class Parser() {
    fun ParseSrtFile(alltext:String):ArrayList<TimeTextDataModel>{
        var timeTextDataModel:TimeTextDataModel
        var InitilSubtitleArray: ArrayList<TimeTextDataModel> = ArrayList()
        val reader = BufferedReader(StringReader(alltext))
        /*
          NewItem will help me to create new TimeTextDataModel and set
          Time and Text later add to this TimeTextDataModel
        */
        var NewItem =false
        var Time=""
        var Text =""
        for (line in reader.readLines()){
            if(line.replace(" ", "").isNotEmpty()){
                try {
                    val pattern = Pattern.compile("[0-9]+:[0-9]+:[0-9]+,[0-9]+-->[0-9]+:[0-9]+:[0-9]+,[0-9]+")
                    val time = pattern.toRegex().find(line.replace(" ",""))?.value!!.split("-->")[0]
                    //here I have Time in formated way 00:00:00 --> 00:00:04 I only get first part 00:00:00
                    Time=time
                    Log.d("Time-->",time)
                    NewItem=true
                }catch (e: NullPointerException){
                    try{
                        val numpattern = Pattern.compile("[0-9]+")
                        var num= numpattern.toRegex().find(line.replace(" ",""))?.value
                        if(num!!.length!=line.replace(" ","").length){
                            //that mmeans it is number in text not index in srt file.
                            Text=line.replace(","," ")
                            Log.d("Text-->",Text)
                            if(NewItem){
                                timeTextDataModel =TimeTextDataModel(Time,Text)
                                InitilSubtitleArray.add(timeTextDataModel)
                                NewItem=false
                            }
                        }
                        //if it is parsed to Int it mean it is only the number that is in SRt file.
                    }catch (e:Exception){
                        //at last probably having Subtitle
                        Text=line.replace(","," ")
                        if(NewItem){
                            timeTextDataModel =TimeTextDataModel(Time,Text)
                            InitilSubtitleArray.add(timeTextDataModel)
                            NewItem=false
                        }
                    }
                }
            }
        }
        return InitilSubtitleArray
    }
}