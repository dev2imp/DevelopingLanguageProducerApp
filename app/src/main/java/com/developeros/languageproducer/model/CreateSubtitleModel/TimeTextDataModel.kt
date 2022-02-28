package com.developeros.languageproducer.model.CreateSubtitleModel
class TimeTextDataModel {
    var Time: String
    var Text: String
    constructor(time: String, text: String) {
        this.Time = time
        this.Text = text
    }
    override fun toString(): String {
        return "{Time:'$Time' <comma> Text:'$Text'}"
    }
}