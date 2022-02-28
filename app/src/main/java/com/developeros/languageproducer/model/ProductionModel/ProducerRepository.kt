package com.developeros.languageproducer.model.ProductionModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.developeros.languageproducer.model.ConsumerModel.ConsumerDataModel
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel
import com.developeros.languageproducer.utils.LooperModel
import com.developeros.languageproducer.utils.Parser
import com.developeros.languageproducer.utils.StringManipulation
import com.developeros.languageproducer.utils.TimeManipulation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
class ProducerRepository {

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
    var ProducerItems:MutableLiveData<ArrayList<ConsumerDataModel>> = MutableLiveData()
    var tempArray:ArrayList<ConsumerDataModel> = ArrayList()
    suspend fun GetItemFromServer(UserToken:String){
        val ref = db.collection("users/${UserToken}/posts")
        ref.get().addOnSuccessListener {
            /*
            Sample Data
            "firstpath" to "english/path",
            "secondpath" to "spanish/path",
            "title" to "title",
            "videoid" to "videoid"
             */
            for (doc in it.documents){
                doc.apply {
                    tempArray.add(
                        ConsumerDataModel(
                            get("title").toString(),
                            get("videoid").toString(),
                            get("firstpath").toString(),
                            get("secondpath").toString(),
                            ""
                            )
                    )
                }
            }
            ProducerItems.postValue(tempArray)
        }
    }
}