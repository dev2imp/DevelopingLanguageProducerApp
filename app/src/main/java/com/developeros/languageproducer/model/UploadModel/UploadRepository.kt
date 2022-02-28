package com.developeros.languageproducer.model.UploadModel
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.StringReader
import java.lang.NullPointerException
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class UploadRepository {
    //Firebase Storage
    var storage:FirebaseStorage
    var ref:StorageReference
    //Firebase FireStore
    var db:FirebaseFirestore
    var FirsLangPath=""
    var SecondLangPath=""
    var FilePath=""
    constructor(){
        storage = Firebase.storage("gs://languages-77045.appspot.com")
        ref = storage.reference
        //Firebase FireStore
        db = Firebase.firestore
    }
    var Error:MutableLiveData<String> = MutableLiveData()
   suspend fun UploadToServer(uploadDataModel: UploadDataModel){
       //the user wants to upload some file too
       if(FileContent.length>0){
           if(FileContent.length<200000){
               var filePost = hashMapOf(
                   "file" to FileContent.replace(" ","")
               )
               db.collection("file").add(filePost).addOnSuccessListener {
                   //Now we know where the file is inserted
                   FilePath=it.path
                   UploadForLangs(uploadDataModel)
               }.addOnFailureListener{
                   Log.d("Failureee",it.toString())
               }
           }else{
               Error.postValue("MaxFilesize200kb")
           }
       }else{
           //User doesnt have file to upload
           UploadForLangs(uploadDataModel)
       }
    }
    fun UploadForLangs(uploadDataModel:UploadDataModel){
        //data to write to firestore
        var post = hashMapOf(
            "user" to Firebase.auth.currentUser!!.uid,
            "title" to uploadDataModel.Title,
            "firstlanguage" to uploadDataModel.FirstLanguage,
            "secondlanguage" to uploadDataModel.SecondLanguage,
            "videoid" to uploadDataModel.VideoID,
            "file" to "${FilePath}"
        )
        /*
        at Most user can select two Languages.
        and we will save the same post for both languages differently
        that is what I do below
         */
        db.collection(uploadDataModel.FirstLanguage.lowercase()).add(post).addOnSuccessListener {
            FirsLangPath=it.path
            if(uploadDataModel.SecondLanguage.length==0){
                AttachToUser(uploadDataModel)
            }
        }
        if(uploadDataModel.SecondLanguage.length>0){
            db.collection(uploadDataModel.SecondLanguage.lowercase()).add(post).addOnSuccessListener {
                SecondLangPath=it.path
                AttachToUser(uploadDataModel)
            }
        }
    }
    fun AttachToUser(uploadDataModel:UploadDataModel){
        //save link to user so we know Who  have shared what.
        var userpost = hashMapOf(
            "firstpath" to FirsLangPath,
            "secondpath" to SecondLangPath,
            "title" to uploadDataModel.Title,
            "videoid" to uploadDataModel.VideoID
        )
        db.document("users/${Firebase.auth.currentUser!!.uid}").collection("posts").add(userpost).addOnSuccessListener {
            Log.d("userpost-->", "${userpost}")
        }
    }
    var FileContent=""
    fun GetFileString(uri: Uri?){
        /*
        we want to get text in file and upload to firestore
        */
        Log.d("UploadRepository->",uri!!.path.toString())
        val file = File(uri?.path.toString())//.split(":")[1]
        FileContent = file.readText()
    }
}