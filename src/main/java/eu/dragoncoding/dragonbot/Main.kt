package eu.dragoncoding.dragonbot

import eu.dragoncoding.dragonbot.utils.TimeUtils.convertFromLongToTimeString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.jvm.JvmStatic

object Main {

    private var logger: Logger
    var shutDown = false

    @JvmStatic
    fun main(args: Array<String>) {

        val startUpTime = Bot.startUp()
        logger.info("Bot started successfully! Startup time: ${convertFromLongToTimeString(startUpTime)}")

        while (!shutDown) {
            Thread.sleep(1000L)
        }

        val shutDownTime = Bot.shutDown()
        logger.info("Bot successfully shut down! Shutdown time: ${convertFromLongToTimeString(shutDownTime)}")
    }


    //Static Initializer
    init {
        //Set path for LogBack HTML-File output before anything else happens
        val os = System.getProperty("os.name")
        System.setProperty("log.path", if (os == "Windows 10") logPathWin else logPathLin)

        logger = LoggerFactory.getLogger(Main::class.java)
    }
}