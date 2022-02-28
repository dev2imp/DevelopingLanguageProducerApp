package com.developeros.languageproducer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developeros.languageproducer.model.RoomDatabse.RoomDatabaseRepository
import com.developeros.languageproducer.model.RoomDatabse.TargetSourceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(val roomDatabaseRepository: RoomDatabaseRepository) : ViewModel() {
    fun InsertLanguages(targetSourceModel: TargetSourceModel) =
        viewModelScope.launch(Dispatchers.IO) {
            roomDatabaseRepository.InsertLanguages(targetSourceModel)
        }
    fun DeleteLanguages() =
        viewModelScope.launch(Dispatchers.IO) {
            roomDatabaseRepository.DeleteLanguages()
        }
    //The language user have selected to get data for.
    var ChosenLangsFromDb: LiveData<TargetSourceModel> = roomDatabaseRepository.ChosenLangsFromDb()

}