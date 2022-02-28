package com.developeros.languageproducer.model.CreateSubtitleModel

import android.widget.ArrayAdapter

class SubtitleDataModel {
    var Time = ""
    var Text = ""
    fun setTimeText(time: String, text: String) {
        this.Time = time
        this.Text = text
    }
}