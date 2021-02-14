package eu.dragoncoding.dragonbot.utils

object TimeUtils {

    @JvmStatic
    fun convertFromLongToTimeString(timeInMs: Long): String {
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
}