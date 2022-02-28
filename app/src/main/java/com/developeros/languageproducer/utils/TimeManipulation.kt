package com.developeros.languageproducer.utils

class TimeManipulation {
    fun TimeToSecond(formated: String): Float {
        //00:00:00,000
        var arr: List<String> = formated.split(":")
        var seconds = 0.0F
        try {
            var Hour = arr.get(0).toFloat()
            var Minute = arr.get(1).toFloat()
            seconds = arr.get(2).split(",")[0].toFloat()
            seconds = seconds + Minute * 60
            seconds = seconds + Hour * 3600
        } catch (e: Exception) {

        }
        return seconds
    }

    fun SecondToTimeFormated(second: Float): String {
        var hour = second / 3600
        var minutes = (second % 3600) / 60
        var seconds = second % 60
        return String.format("%.0f:%.0f:%.3f",hour,minutes,seconds)
    }
}