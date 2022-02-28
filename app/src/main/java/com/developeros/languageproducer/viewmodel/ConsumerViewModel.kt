package com.developeros.languageproducer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developeros.languageproducer.model.ConsumerModel.ConsumerRepository
import com.developeros.languageproducer.model.RoomDatabse.RoomDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConsumerViewModel(val consumerRepository: ConsumerRepository,val roomDatabaseRepository: RoomDatabaseRepository):ViewModel() {
    var ConsumerItems =consumerRepository.ConsumerItems
    fun GetItemFromServer(langs:String) =viewModelScope.launch(Dispatchers.IO) {
        consumerRepository.GetItemFromServer(langs)
    }
    var GetChosenLangFromRoom=roomDatabaseRepository.ChosenLangsFromDb()
    fun ReadFileFromServer(path:String) = viewModelScope.launch(Dispatchers.IO) {
        consumerRepository.ReadFileFromServer(path)
    }
    var CurrentItem = consumerRepository.CurrentItem
    fun SetCurrentSub(index:Int) = viewModelScope.launch(Dispatchers.IO) {
        consumerRepository.SetCurrentSub(index)
    }
    var loopModel= consumerRepository.loopModel
    fun setLoopArray(index: Int) = viewModelScope.launch(Dispatchers.IO){
        consumerRepository.SetLoopArray(index)
    }
}