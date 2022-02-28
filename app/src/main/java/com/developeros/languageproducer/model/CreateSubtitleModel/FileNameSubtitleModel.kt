package com.developeros.languageproducer.model.CreateSubtitleModel

class FileNameSubtitleModel {
    var fileName = ""
    var subtitlecontent = ""

    constructor(filename: String, subtitlecontent: String) {
        this.subtitlecontent = subtitlecontent
        this.fileName = filename
    }
}