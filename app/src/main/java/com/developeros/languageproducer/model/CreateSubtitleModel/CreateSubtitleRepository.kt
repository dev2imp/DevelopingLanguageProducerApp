package com.developeros.languageproducer.model.CreateSubtitleModel
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.developeros.languageproducer.model.RoomDatabse.AppSaveDataModel
import com.developeros.languageproducer.utils.LooperModel
import com.developeros.languageproducer.utils.Parser
import com.developeros.languageproducer.utils.StringManipulation
import com.developeros.languageproducer.utils.TimeManipulation
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.StringReader
import java.lang.NullPointerException
import java.util.regex.Pattern
class CreateSubtitleRepository {
    var VideoId = MutableLiveData<String>()
    suspend fun YoutubeUrlToVideoId(url: String) {
        var videoUrlManipulator = VideoUrlManipulator()
        VideoId.postValue(videoUrlManipulator.UrltoUrlId(url))
    }
    var SignleItem = MutableLiveData<TimeTextDataModel>()
    var SubtitleArray: ArrayList<TimeTextDataModel> = ArrayList()
    suspend fun addToSubtileArray(timeTextDataModel: TimeTextDataModel) {
        SubtitleArray.add(timeTextDataModel)
        SignleItem.postValue(timeTextDataModel)
    }
    suspend fun updateItemAt(timeTextDataModel: TimeTextDataModel, position: Int) {
        try {
            SubtitleArray[position] = timeTextDataModel
        } catch (e: Exception) {

        }
    }
    suspend fun removeAt(position: Int) {
        if (position < SubtitleArray.size) {
            SubtitleArray.removeAt(position)
        }
    }
    /*
    here we create another array to initilize InitilSubtitleArray
    and get subs fdrom db and put both InitilSubtitleArray and  SubtitleArray
    layer we never use InitilSubtitleArray because there is a Observer on InitilzedArray
     */
    var InitilSubtitleArray: ArrayList<TimeTextDataModel> = ArrayList()
    var InitilzedArray: MutableLiveData<ArrayList<TimeTextDataModel>> = MutableLiveData()
    suspend fun InitilizeArray(appSaveDataModel: AppSaveDataModel) {
        try {
            var string = appSaveDataModel.array

            var items = string.split(",")
            for (item in items) {
                var timeTextDataModel = StringManipulation().TagToComma(item)
                var time=""
                var text=""
                try {
                    time= JSONObject(timeTextDataModel).get("Time").toString()
                   text= JSONObject(timeTextDataModel).get("Text").toString()
                }catch (e:Exception){
                    Log.d("InitilzedArray-->Text->",text)
                }
                InitilSubtitleArray.add(
                    TimeTextDataModel(time,text)
                )
                SubtitleArray.add(
                    TimeTextDataModel(time,text)
                )
            }
            InitilzedArray.postValue(InitilSubtitleArray)
        } catch (e: Exception) {
        }
    }
    suspend fun ResetSubArrayList() {
        SubtitleArray = ArrayList()
    }
    //fun below will set start and end second to loop between if switch is on
    var loopModel: MutableLiveData<LooperModel> = MutableLiveData()
    suspend fun SetLoopArray(position: Int) {
        var start = 0.0F
        var end = 0.5F
        if (position > 0){
            try {
                val item = SubtitleArray.get(position)
                start = TimeManipulation().TimeToSecond(item.Time)
            } catch (e: java.lang.Exception) {
            }
        }
        if (SubtitleArray.size > position + 1) {
            try {
                val item = SubtitleArray.get(position + 1)
                end = TimeManipulation().TimeToSecond(item.Time)
            } catch (e: Exception) {
            }
        }
        loopModel.postValue(LooperModel(start, end))
    }
    var ShowTimeNow: MutableLiveData<String> = MutableLiveData()
    fun SetUpShowTimeNow(second: Float) {
        ShowTimeNow.postValue(TimeManipulation().SecondToTimeFormated(second))
    }
    var content: MutableLiveData<FileNameSubtitleModel> = MutableLiveData()
    fun onExportFile(filename: String) {
        /*
            get SubtitleArrayList concert arraylist to srt file format.
            1
            00:04:00,400
            this is format of  srt file
         */
        var start: String
        var end: String
        var counter = 1
        var subs = ""
        for (sub in SubtitleArray){
            subs += "\n$counter\n"
            start = sub.Time
            if (SubtitleArray.size > counter) {
                val nextsub = SubtitleArray.get(counter)
                end = nextsub.Time
            }else{
                end = start
            }
            subs += " ${start} --> ${end}\n ${sub.Text}"
            counter++
        }
        val fileNameSubtitleModel = FileNameSubtitleModel(filename, subs)
        content.postValue(fileNameSubtitleModel)
    }
    fun InitAlizeOnImportFile(uri: Uri?){
        /*
         there is an observable on InitializedArray so I need to get Timetextdata model and
         and put it an InitializeSubtitleArray later post to InitializedArray.
        */
        InitilSubtitleArray = ArrayList()
        val file = File(uri?.path.toString().split(":/")[1])
        val alltext = file.readText()
        //parsing srt file
        InitilSubtitleArray=Parser().ParseSrtFile(alltext)
        //posting array for observer to take action
        InitilzedArray.postValue(InitilSubtitleArray)
        SubtitleArray = InitilSubtitleArray
    }
}

