package com.developeros.languageproducer.model.CreateSubtitleModel

class VideoUrlManipulator {
    //getting url id from given url
    fun UrltoUrlId(url: String): String {
        try {
            var uri: ArrayList<String> = url.split("/") as ArrayList<String>
            return uri.get(uri.size - 1)
        }catch (e: Exception){
            return "null"
        }
    }
}