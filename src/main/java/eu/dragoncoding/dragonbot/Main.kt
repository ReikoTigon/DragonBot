package eu.dragoncoding.dragonbot

import kotlin.jvm.JvmStatic

object Main {

    var shutDown = false

    @JvmStatic
    fun main(args: Array<String>) {
        Bot.startUp()

        while (!shutDown) {
            Thread.sleep(1000L)
        }

        Bot.shutDown()
    }


    //Static Initializer
    init {
        //Set path for LogBack HTML-File output before anything else happens
        val os = System.getProperty("os.name")
        System.setProperty("log.path", if (os == "Windows 10") logPathWin else logPathLin)
    }
}