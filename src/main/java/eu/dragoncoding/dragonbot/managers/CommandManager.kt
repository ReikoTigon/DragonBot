package eu.dragoncoding.dragonbot.managers

import eu.dragoncoding.dragonbot.commands.management.*
import eu.dragoncoding.dragonbot.commands.music.*
import eu.dragoncoding.dragonbot.commands.utilities.BotInfo
import eu.dragoncoding.dragonbot.commands.utilities.Clear
import eu.dragoncoding.dragonbot.commands.utilities.Embed
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import net.dv8tion.jda.api.entities.Message
import java.util.concurrent.ConcurrentHashMap

object CommandManager {

    private val commands: ConcurrentHashMap<String, Command> = ConcurrentHashMap<String, Command>()

    fun perform(command: String, message: Message, subString: Int, type: CommandType) {
        val serverCommand: Command
        if (commands[command.toLowerCase()] != null) {

            serverCommand = commands[command.toLowerCase()]!!
            serverCommand.performCommand(message, subString, type)
        }
    }

    init {
        //Utility Commands
        addCommand("clear", Clear(), CommandType.NORMAL, CommandType.BOTCHANNEL)
        addCommand("embed", Embed(), CommandType.NORMAL, CommandType.BOTCHANNEL)
        addCommand("botinfo", BotInfo(), CommandType.NORMAL, CommandType.BOTCHANNEL, CommandType.PRIVATE)

        //Management Commands
        addCommand("botchannel", BotChannel(), CommandType.NORMAL, CommandType.BOTCHANNEL)
        addCommand("settings", Settings(), CommandType.BOTCHANNEL)
        addCommand("musicchannel", MusicChannel(), CommandType.BOTCHANNEL)
        addCommand("dashboard", Dashboard(), CommandType.BOTCHANNEL)
        addCommand("nowplaying", SNowPlaying(), CommandType.BOTCHANNEL)
        addCommand("deletecommands", SDeleteCommands(), CommandType.BOTCHANNEL)


        //Music Commands
        addCommand("play", Play(), CommandType.NORMAL, CommandType.DASHBOARD)
        addCommand("stop", Stop(), CommandType.NORMAL, CommandType.DASHBOARD)
        addCommand("skip", Skip(), CommandType.NORMAL, CommandType.DASHBOARD)
        addCommand("queue", Queue(), CommandType.NORMAL, CommandType.DASHBOARD)
        addCommand("volume", Volume(), CommandType.NORMAL, CommandType.DASHBOARD)
        addCommand("pause", Pause(), CommandType.NORMAL, CommandType.DASHBOARD)
        addCommand("last", Last(), CommandType.NORMAL, CommandType.DASHBOARD)
    }

    fun getCommandList(): List<String> {
        return commands.keys().toList()
    }
    private fun addCommand(cmd: String, commandClass: Command, vararg types: CommandType) {
        this.commands[cmd] = commandClass

        for (type in types) {
            when(type) {
                CommandType.NORMAL -> CommandExecutor.addNormalCommand(cmd)
                CommandType.BOTCHANNEL -> CommandExecutor.addConfigCommand(cmd)
                CommandType.PRIVATE -> CommandExecutor.addPrivateCommand(cmd)
                CommandType.DASHBOARD -> CommandExecutor.addDashboardCommand(cmd)
            }
        }
    }
}