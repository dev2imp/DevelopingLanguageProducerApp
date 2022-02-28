package com.developeros.languageproducer.model.RoomDatabse
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel
@Entity(tableName = "app_save_data_model")
class AppSaveDataModel() {
    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0
    var key = ""
    var array = ""
    constructor(key: String, arr: ArrayList<TimeTextDataModel>) : this() {
        this.key = key
        this.array = arr.toString()
    }
    fun setId(id: Int) {
        this.id = id
    }
    fun getId(): Int {
        return id
    }
    override fun toString(): String {
        return "AppSaveDataModel(id:$id, key:'$key', array:$array)"
    }
}
@Entity(tableName = "target_source_language_model")
class TargetSourceModel(){
    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0
    var sourceArray = ""
    constructor(sourceArray: ArrayList<String>) : this() {
        this.sourceArray = sourceArray.toString()
    }
    fun setId(id: Int) {
        this.id = id
    }
    fun getId(): Int {
        return id
     }
}
