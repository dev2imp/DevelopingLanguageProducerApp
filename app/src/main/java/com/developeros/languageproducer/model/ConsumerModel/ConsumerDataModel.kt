package com.developeros.languageproducer.model.ConsumerModel

import android.net.Uri

class ConsumerDataModel {
    var Title:String
    var VideoID:String
    var FirstLanguage:String
    var SecondLanguage:String
    var FilePath: String
    constructor(
        Title: String,
        VideoID: String,
        FirstLanguage: String,
        SecondLanguage: String,
        FilePath: String
    ) {
        this.Title = Title
        this.VideoID = VideoID
        this.FirstLanguage = FirstLanguage
        this.SecondLanguage = SecondLanguage
        this.FilePath = FilePath
    }

    override fun toString(): String {
        return "{Title:'$Title', VideoID:'$VideoID', FirstLanguage:'$FirstLanguage', SecondLanguage:'$SecondLanguage', FilePath:'$FilePath'}"
    }

}