package eu.dragoncoding.dragonbot.utils.tasks

import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.statPathLin
import eu.dragoncoding.dragonbot.statPathWin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.floor

class StatCollector : TimerTask() {

    private val maxTime = 60
    private val consoleInterval = 15
    private val tableInterval = 30
    private val logInterval = 5

    override fun run() {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formattedTime = currentTime.format(formatter)
        val guildInfo = guildCounter()
        
        if (isFullNumber(toConsole, consoleInterval)) {
            writeToConsole(formattedTime, arrayOf(guildInfo[0], guildInfo[1], guildInfo[2], commandCounter()))
        }
        if (isFullNumber(toConsole, tableInterval)) {
            writeToTable(formattedTime, arrayOf(guildInfo[0], guildInfo[1], guildInfo[2], commandCounter()))
        }
        if (isFullNumber(toConsole, logInterval)) {
            writeToLogFile(formattedTime, arrayOf(guildInfo[0], guildInfo[1], guildInfo[2], commandCounter()))
        }

        if (toConsole == maxTime) {
            toConsole = 1
        } else {
            toConsole++
        }
    }

    private fun guildCounter(): Array<Int>{
        val countAll = DGuild.getAll().size
        val countActive = DGuild.getAllActive().size
        val countInactive = countAll - countActive

        return arrayOf(countAll, countActive, countInactive)
    }

    private fun commandCounter(): Int {
        val guilds = DGuild.getAll()
        var commandsUsed = 0

        for (guild in guilds) {
            commandsUsed += guild.commandsUsed
        }

        return commandsUsed
    }

    private fun writeToTable(time: String, array: Array<Int>) {
        val os = System.getProperty("os.name")
        val path = if (os == "Windows 10") statPathWin else statPathLin

        val file = File(path)
        val new = !file.exists()

        if (new) {
            file.createNewFile()
            val writer = PrintWriter(file)

            writer.append("<style>\n" +
                          "  #cssTable td \n" +
                          "  {\n" +
                          "      text-align: center; \n" +
                          "      vertical-align: middle;\n" +
                          "      border-style: solid; \n" +
                          "      border-width: thin;\n" +
                          "  }\n" +
                          "</style>\n\n" +
                          "<table style=\"width:auto; border-style: solid; border-width: thin;\" id=\"cssTable\">\n" +
                          "  <tr>\n" +
                          "    <th>Time</th>\n" +
                          "    <th>Total Guilds</th>\n" +
                          "    <th>Joined Guilds</th>\n" +
                          "    <th>Left Guilds</th>\n" +
                          "    <th>Total used Commands</th>\n" +
                          "  </tr>\n")

            writer.close()
        }

        file.appendText("  <tr>\n" +
                "    <td>${time}</td>\n" +
                "    <td>${array[0]}</td>\n" +
                "    <td>${array[1]}</td>\n" +
                "    <td>${array[2]}</td>\n" +
                "    <td>${array[3]}</td>\n" +
                "  </tr>\n")
    }
    private fun writeToConsole(time: String, array: Array<Int>) {
        //[dd.MM.yyyy HH:mm] => Total Guilds: [0000], Joined Guilds: [0000], Left Guilds: [0000], Total Commands Used: [0000000]

        val builder = StringBuilder()
        builder.append("[${time}]")
        builder.append(" => ")
        builder.append("Total Guilds: [${fillUntilXLong('0', array[0].toString(), 4)}], ")
        builder.append("Joined Guilds: [${fillUntilXLong('0', array[1].toString(), 4)}], ")
        builder.append("Left Guilds: [${fillUntilXLong('0', array[2].toString(), 4)}], ")
        builder.append("Total Commands Used: [${fillUntilXLong('0', array[3].toString(), 7)}]")

        val logger: Logger = LoggerFactory.getLogger("statsConsole")
        logger.info(builder.toString())
    }
    private fun writeToLogFile(time: String, array: Array<Int>) {
        //[dd.MM.yyyy HH:mm] => Total Guilds: [0000], Joined Guilds: [0000], Left Guilds: [0000], Total Commands Used: [0000000]

        val builder = StringBuilder()
        builder.append("[${time}]")
        builder.append(" => ")
        builder.append("Total Guilds: [${fillUntilXLong('0', array[0].toString(), 4)}], ")
        builder.append("Joined Guilds: [${fillUntilXLong('0', array[1].toString(), 4)}], ")
        builder.append("Left Guilds: [${fillUntilXLong('0', array[2].toString(), 4)}], ")
        builder.append("Total Commands Used: [${fillUntilXLong('0', array[3].toString(), 7)}]")

        val logger: Logger = LoggerFactory.getLogger("statsFile")
        logger.info(builder.toString())
    }

    private fun fillUntilXLong(fillWith: Char, whatToFill: String, lengthToFill: Int):String {
        return if (whatToFill.length >= lengthToFill) {
            whatToFill
        } else {
            var temp = whatToFill
            while (temp.length != lengthToFill) {
                temp = "$fillWith$temp"
            }
            temp
        }
    }

    companion object {
        var toConsole = 1
        private fun isFullNumber(interval: Int, input: Int): Boolean {
            val out: Double = interval.toDouble() / input.toDouble()

            return floor(out) == out
        }
    }
}