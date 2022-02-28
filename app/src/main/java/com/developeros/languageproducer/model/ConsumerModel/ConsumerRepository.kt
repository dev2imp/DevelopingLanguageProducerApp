package com.developeros.languageproducer.model.ConsumerModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel
import com.developeros.languageproducer.utils.LooperModel
import com.developeros.languageproducer.utils.Parser
import com.developeros.languageproducer.utils.StringManipulation
import com.developeros.languageproducer.utils.TimeManipulation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
class ConsumerRepository {
    var firstlanguage=""
    var secondlanguage=""
    var file=""
    var videoid=""
    var user=""
    var title=""
    //Firebase FireStore
    var db: FirebaseFirestore
    constructor(){
        //Firebase FireStore
        db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings=settings
    }
    var ConsumerItems:MutableLiveData<List<ConsumerDataModel>> = MutableLiveData()
    var tempArray:ArrayList<ConsumerDataModel> = ArrayList()
    suspend fun GetItemFromServer(langs:String){
        var langArr=langs.split(",")
        for(lang in langArr){
            val ref = db.collection(lang.lowercase())
            ref.get().addOnSuccessListener {
                for (doc in it.documents){
                    firstlanguage= doc.data!!.get("firstlanguage").toString()
                    secondlanguage=doc.data!!.get("secondlanguage").toString()
                    file=doc.data!!.get("file").toString()
                    videoid=doc.data!!.get("videoid").toString()
                    user=doc.data!!.get("user").toString()
                    title=doc.data!!.get("title").toString()
                    var consumerDataModel = ConsumerDataModel(title,videoid,firstlanguage,secondlanguage,file)
                    tempArray.add(consumerDataModel)
                }
                ConsumerItems.postValue(tempArray)
            }
        }
    }
    //current subtitle to show to use
    var CurrentItem:MutableLiveData<ConsumerNextTimeCurrentText> = MutableLiveData()
    //all subs for current video will be stored here.
    var SubtitleArray: ArrayList<TimeTextDataModel> = ArrayList()
   suspend fun ReadFileFromServer(path:String){
       var fileid=""
       try{
           fileid=path.split("/")[1]
           Log.d("fileid->",fileid)
       }catch (e:Exception){
           Log.d("fileid->",e.message.toString())
       }
       Log.d("fileid->",fileid)
       try{
           db.collection("file").document(fileid).get().addOnSuccessListener {
               var text = it.data
               SubtitleArray= Parser().ParseSrtFile(text!!.get("file").toString())
           }
       }catch (e:Exception){

       }
    }
   suspend fun SetCurrentSub(index:Int){
       /*
       SubtitleArray{
       time:10,text:"time is 10 secon"//0
       time:20,text:"time is 20 secon"//1
       currentItem is at index 0
       the time I have to keep check is at index 2
       as current second passes time at index 2 I have to increment index
       //By default index =0
       TODO
        create ConsumerTimeTextDataModel that will have current text,and next time and index in seconds.
       }
        */
       if(SubtitleArray.size>index+1){
           try {
               var nextTime=TimeManipulation().TimeToSecond(SubtitleArray.get(index+1).Time)
               var currentTime=TimeManipulation().TimeToSecond(SubtitleArray.get(index).Time)
               var consumerNextTimeCurrentText = ConsumerNextTimeCurrentText(index,nextTime,SubtitleArray.get(index).Text,currentTime)
               CurrentItem.postValue(consumerNextTimeCurrentText)
               Log.d("nextTime->",nextTime.toString())
           }catch (e:Exception){
               Log.d("CurrentIException",e.toString())
           }
       }
    }
    //fun below will set start and end second to loop between if switch is on
    var loopModel: MutableLiveData<LooperModel> = MutableLiveData()
    suspend fun SetLoopArray(position: Int) {
        var start = 0.0F
        var end = 1.0F
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
}