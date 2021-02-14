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

class StatCollector : TimerTask() {
    override fun run() {
        val logger: Logger = LoggerFactory.getLogger(StatCollector::class.java)

        val builder = StringBuilder()

        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formattedTime = currentTime.format(formatter)

        val guildInfo = guildCounter()

        builder.append("\nHourly Stats($formattedTime)\n")
        builder.append("----------------------------\n")
        builder.append("Total Guilds: ${guildInfo[0]} (Joined:${guildInfo[1]}, Left:${guildInfo[2]})\n")
        builder.append("Total Commands Used: ${commandCounter()}")

        logger.info(builder.toString())

        writeToTable(formattedTime, arrayOf(guildInfo[0], guildInfo[1], guildInfo[2], commandCounter()))
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
}