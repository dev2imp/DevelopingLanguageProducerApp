package com.developeros.languageproducer.model.RoomDatabse

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDao {
    @Insert
    fun InsertFile(appSaveDataModel: AppSaveDataModel)

    @Query("DELETE  FROM app_save_data_model")
    fun DeleteInDb()

    @Update
    fun UpdateFile(appSaveDataModel: AppSaveDataModel)

    @Query("SELECT * FROM app_save_data_model")
    fun getAllFiles(): LiveData<AppSaveDataModel>

    //target_source_language_model dao
    @Insert
    fun InsertLanguage(targetSourceModel: TargetSourceModel)

    @Query("DELETE  FROM target_source_language_model")
    fun DeleteLanguages()

    @Query("SELECT * FROM target_source_language_model")
    fun getLanguages(): LiveData<TargetSourceModel>
}