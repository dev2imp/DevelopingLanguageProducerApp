package com.developeros.languageproducer.model.RoomDatabse
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
class RoomDatabaseRepository(val appDao: AppDao) {
    var allFile: LiveData<AppSaveDataModel> = appDao.getAllFiles()
    @WorkerThread
    suspend fun InsertFile(appSaveDataModel: AppSaveDataModel){
        appDao.DeleteInDb()
        try{
            appDao.InsertFile(appSaveDataModel)
        }catch (e:Exception){
        }
    }
    @WorkerThread
    suspend fun DeleteInDb() {
        appDao.DeleteInDb()
    }
    //Setting Repository for SettingsViewModel
    //all languages are fetched from db
    fun ChosenLangsFromDb():LiveData<TargetSourceModel>{
      return  appDao.getLanguages()
    }
    //Insert languages to db
    @WorkerThread
    suspend fun InsertLanguages(targetSourceModel: TargetSourceModel){
        appDao.DeleteLanguages()
        appDao.InsertLanguage(targetSourceModel)
    }
    @WorkerThread
    suspend fun DeleteLanguages(){
        appDao.DeleteLanguages()
    }
}