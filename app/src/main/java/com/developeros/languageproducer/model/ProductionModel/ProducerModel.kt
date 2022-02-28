package com.developeros.languageproducer.model.ProductionModel

class ProducerModel {
    var firstpath:String
    var secondpath:String
    var title:String
    var videoid:String
    constructor(firstpath: String, secondpath: String, title: String, videoid: String) {
        this.firstpath = firstpath
        this.secondpath = secondpath
        this.title = title
        this.videoid = videoid
    }
}