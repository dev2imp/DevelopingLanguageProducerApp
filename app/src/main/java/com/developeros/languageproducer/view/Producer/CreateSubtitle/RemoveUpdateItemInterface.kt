package com.developeros.languageproducer.view.Producer.CreateSubtitle
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel

interface RemoveUpdateItemInterface {
    fun RemoveItemAt(position: Int)
    fun notifyChange(item: TimeTextDataModel, position: Int)
}