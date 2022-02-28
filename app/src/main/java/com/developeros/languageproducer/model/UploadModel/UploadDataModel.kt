package com.developeros.languageproducer.model.UploadModel

import android.net.Uri

class UploadDataModel {
    var Title=""
    var VideoID=""
    var FirstLanguage=""
    var SecondLanguage=""
    var FilePath:Uri?
    constructor(
        Title: String,
        VideoID: String,
        FirstLanguage: String,
        SecondLanguage: String,
        FilePath: Uri?
    ) {
        this.Title = Title
        this.VideoID = VideoID
        this.FirstLanguage = FirstLanguage
        this.SecondLanguage = SecondLanguage
        this.FilePath = FilePath
    }


}