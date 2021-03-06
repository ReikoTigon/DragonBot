package eu.dragoncoding.dragonbot.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun convertLongToDateTimeString(timeInMs: Long, format: String): String {
        val dateTime = Date(timeInMs)
        return SimpleDateFormat("yyyy.MM.dd HH:mm").format(dateTime)
    }

    fun longToTimeStringMs(timeInMs: Long): String {
        val milliseconds = timeInMs / 1000 % 1000
        val seconds = timeInMs / 1000 % 60
        val minutes = timeInMs / (1000 * 60) % 60
        val hours = timeInMs / (1000 * 60 * 60) % 24
        val days = timeInMs / (1000 * 60 * 60 * 24)

        return when {
            days != 0L -> {
                "${days}d ${hours}h ${minutes}min ${seconds}s ${milliseconds}ms"
            }
            hours != 0L -> {
                "${hours}h ${minutes}min ${seconds}s ${milliseconds}ms"
            }
            minutes != 0L -> {
                "${minutes}min ${seconds}s ${milliseconds}ms"
            }
            seconds != 0L -> {
                "${seconds}s ${milliseconds}ms"
            }
            milliseconds != 0L -> {
                "${milliseconds}ms"
            }
            else -> {
                "0ms"
            }
        }
    }
    fun longToTimeStringS(timeInMs: Long): String {
        val seconds = timeInMs / 1000 % 60
        val minutes = timeInMs / (1000 * 60) % 60
        val hours = timeInMs / (1000 * 60 * 60) % 24
        val days = timeInMs / (1000 * 60 * 60 * 24)

        return when {
            days != 0L -> {
                "${days}d ${hours}h ${minutes}min ${seconds}s"
            }
            hours != 0L -> {
                "${hours}h ${minutes}min ${seconds}s"
            }
            minutes != 0L -> {
                "${minutes}min ${seconds}s"
            }
            seconds != 0L -> {
                "${seconds}s"
            }
            else -> {
                "0s"
            }
        }
    }
    fun longToTimeStringMin(timeInMs: Long): String {
        val minutes = timeInMs / (1000 * 60) % 60
        val hours = timeInMs / (1000 * 60 * 60) % 24
        val days = timeInMs / (1000 * 60 * 60 * 24)

        return when {
            days != 0L -> {
                "${days}d ${hours}h ${minutes}min"
            }
            hours != 0L -> {
                "${hours}h ${minutes}min"
            }
            minutes != 0L -> {
                "${minutes}min"
            }
            else -> {
                "0min"
            }
        }
    }
    fun longToTimeStringH(timeInMs: Long): String {
        val hours = timeInMs / (1000 * 60 * 60) % 24
        val days = timeInMs / (1000 * 60 * 60 * 24)

        return when {
            days != 0L -> {
                "${days}d ${hours}h"
            }
            hours != 0L -> {
                "${hours}h"
            }
            else -> {
                "0h"
            }
        }
    }
    fun longToTimeStringD(timeInMs: Long): String {
        val days = timeInMs / (1000 * 60 * 60 * 24)

        return when {
            days != 0L -> {
                "${days}d"
            }
            else -> {
                "0d"
            }
        }
    }
}
