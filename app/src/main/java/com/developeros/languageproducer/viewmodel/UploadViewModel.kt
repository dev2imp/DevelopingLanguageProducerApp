package com.developeros.languageproducer.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developeros.languageproducer.model.RoomDatabse.RoomDatabaseRepository
import com.developeros.languageproducer.model.UploadModel.UploadDataModel
import com.developeros.languageproducer.model.UploadModel.UploadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UploadViewModel(val uploadRepository: UploadRepository,val roomDatabaseRepository: RoomDatabaseRepository):ViewModel() {
    fun UploadToServer(uploadDataModel: UploadDataModel) = viewModelScope.launch(Dispatchers.IO){
        uploadRepository.UploadToServer(uploadDataModel)
    }
    var error = uploadRepository.Error
    fun GetFileString(uri: Uri?) = viewModelScope.launch(Dispatchers.IO){
        uploadRepository.GetFileString(uri)
    }
}