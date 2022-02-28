package com.developeros.languageproducer.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.developeros.languageproducer.model.CreateSubtitleModel.CreateSubtitleRepository
import com.developeros.languageproducer.model.RoomDatabse.AppDao
import com.developeros.languageproducer.model.RoomDatabse.AppSaveDataModel
import com.developeros.languageproducer.model.RoomDatabse.RoomDatabaseRepository

class CreateSubtitleViewModel(
    val createSubtitleRepository: CreateSubtitleRepository,
    val roomDatabaseRepository: RoomDatabaseRepository
) : ViewModel() {
    var VideoId: LiveData<String> = createSubtitleRepository.VideoId
    fun YoutubeUrlToVideoId(url: String) = viewModelScope.launch(Dispatchers.IO) {
        createSubtitleRepository.YoutubeUrlToVideoId(url)
    }

    var SignleItem: LiveData<TimeTextDataModel> = createSubtitleRepository.SignleItem
    fun AddToSubtileArray(timeTextDataModel: TimeTextDataModel) =
        viewModelScope.launch(Dispatchers.IO) {
            createSubtitleRepository.addToSubtileArray(timeTextDataModel)
        }

    fun UpdateItemAt(timeTextDataModel: TimeTextDataModel, position: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            createSubtitleRepository.updateItemAt(timeTextDataModel, position)
        }

    fun RemoveAt(position: Int) = viewModelScope.launch(Dispatchers.IO) {
        createSubtitleRepository.removeAt(position)
    }

    //Database Repository
    fun InsertFile(key: String) = viewModelScope.launch(Dispatchers.IO) {
        if(createSubtitleRepository.SubtitleArray.size>0){
            var appSaveDataModel = AppSaveDataModel(key, createSubtitleRepository.SubtitleArray)
            roomDatabaseRepository.InsertFile(appSaveDataModel)
        }
    }
    fun DeleteInDb() = viewModelScope.launch(Dispatchers.IO) {
        roomDatabaseRepository.DeleteInDb()
    }
    var InitilzedArray: LiveData<ArrayList<TimeTextDataModel>> =
        createSubtitleRepository.InitilzedArray
    fun InitilizeArray(appSaveDataModel: AppSaveDataModel) = viewModelScope.launch(Dispatchers.IO){
        createSubtitleRepository.InitilizeArray(appSaveDataModel)
    }
    fun ResetSubArrayList()= viewModelScope.launch(Dispatchers.IO) {
        createSubtitleRepository.ResetSubArrayList()
    }
    //as we get subs from db
    var AllFiles: LiveData<AppSaveDataModel> = roomDatabaseRepository.allFile

    var looperModel=createSubtitleRepository.loopModel
    fun setUpLoopModel(position: Int) =viewModelScope.launch(Dispatchers.IO) {
        createSubtitleRepository.SetLoopArray(position)
    }

    var ShowTimeNow= createSubtitleRepository.ShowTimeNow
    fun SetUpShowTimeNow(second:Float){
        createSubtitleRepository.SetUpShowTimeNow(second)
    }
    var content= createSubtitleRepository.content
    fun onExportFile(filename:String){
        createSubtitleRepository.onExportFile(filename)
    }
    fun InitAlizeOnImportFile(uri: Uri?) = viewModelScope.launch(Dispatchers.IO) {
        createSubtitleRepository.InitAlizeOnImportFile(uri)
    }
}