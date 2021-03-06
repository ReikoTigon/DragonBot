package eu.dragoncoding.dragonbot

import eu.dragoncoding.dragonbot.utils.TimeUtils.longToTimeStringMs
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.system.exitProcess


object Main {

    private var logger: Logger
    var shutDown = false
    var restart = false

    @JvmStatic
    fun main(args: Array<String>) {

        val startUpTime = Bot.startUp()
        logger.info("Bot started successfully! Startup time: ${longToTimeStringMs(startUpTime)}")

        while (!shutDown && !restart) {
            Thread.sleep(1000L)
        }

        if (restart) {
            try {
                val response: String = Bot.restart()
                logger.info(response)
            } catch (e: IOException) {
                logger.error(e.message, e)
            }
        }

        val shutDownTime = Bot.shutDown()
        logger.info("Bot successfully shut down! Shutdown time: ${longToTimeStringMs(shutDownTime)}")

        exitProcess(0)
    }


    //Static Initializer
    init {
        //Set path for LogBack HTML-File output before anything else happens
        val os = System.getProperty("os.name")
        System.setProperty("log.path", if (os == "Windows 10") logPathWin else logPathLin)

        logger = LoggerFactory.getLogger(Main::class.java)
    }


}