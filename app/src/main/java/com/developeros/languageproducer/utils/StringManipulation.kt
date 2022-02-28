package com.developeros.languageproducer.utils

class StringManipulation {
    fun TagToComma(string: String): String {
        return string.replace("<comma>", ",").replace("[", "").replace("]", "")
    }
    fun QuoteToTag(string: String):String{
        return string.replace("'","<quote>")
    }
    fun TagToQuote(string: String):String{
        return string.replace("<quote>","'")
    }
}