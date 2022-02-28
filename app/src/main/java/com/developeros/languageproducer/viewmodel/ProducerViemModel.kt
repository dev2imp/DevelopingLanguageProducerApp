package com.developeros.languageproducer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developeros.languageproducer.model.ConsumerModel.ConsumerDataModel
import com.developeros.languageproducer.model.ProductionModel.ProducerModel
import com.developeros.languageproducer.model.ProductionModel.ProducerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProducerViemModel(val producerRepository: ProducerRepository):ViewModel() {

    fun GetItemsFromServer(token:String) = viewModelScope.launch(Dispatchers.IO) {
        producerRepository.GetItemFromServer(token);
    }
    var AllItems:LiveData<ArrayList<ConsumerDataModel>> = producerRepository.ProducerItems;


}