package eu.dragoncoding.dragonbot.managers

import eu.dragoncoding.dragonbot.defaultPrefix
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import net.dv8tion.jda.api.entities.Message
import java.util.*

object CommandExecutor {

    private val nonSpecificCommands: ArrayList<String> = ArrayList()
    private val botChannelCommands: ArrayList<String> = ArrayList()
    private val privateCommands: ArrayList<String> = ArrayList()
    private val dashboardCommands: ArrayList<String> = ArrayList()

    fun performGuildCmd(dGuild: DGuild, msg: Message, type: CommandType) {
        val message = msg.contentDisplay
        val prefix = startsWithPrefix(message, dGuild.prefix)

        if (prefix != -1) {
            if (message.length > prefix) {

                val command = message.substring(prefix).split(" ".toRegex())[0].toLowerCase()

                if (isCommand(command)) {
                    when (validateCommand(type, command, msg)) {
                        0 -> {
                            CommandManager.perform(command, msg, prefix, type)
                            dGuild.incrementCommands()
                        }
                        1 -> {
                            //ToDo ErrorMessage -> Use Dashboard
                            println("ToDo ErrorMessage -> Use Dashboard")
                        }
                        2 -> {
                            //ToDo ErrorMessage -> Command not allowed in this Channel
                            println("ToDo ErrorMessage -> Command not allowed in this Channel")
                        }
                    }
                } else {
                    //ToDo ErrorMessage -> Unknown Command
                }
            }
        }
    }

    fun performPrivateCmd(msg: Message) {
        val message = msg.contentDisplay
        val prefix = startsWithPrefix(message, defaultPrefix)

        if (prefix != -1) {
            if (message.length > prefix) {

                val command = message.substring(prefix).split(" ".toRegex())[0].toLowerCase()

                if (isCommand(command)) {
                    if (validateCommand(CommandType.PRIVATE, command, msg) == 0) {

                        CommandManager.perform(command, msg, prefix, CommandType.PRIVATE)
                    } else {
                        //ToDo ErrorMessage -> Command not allowed in Private Channel
                    }
                } else {
                    //ToDo ErrorMessage -> Unknown Command
                }
            }
        }
    }


    fun addNormalCommand(cmd: String) {
        nonSpecificCommands.add(cmd)
    }
    fun addConfigCommand(cmd: String) {
        botChannelCommands.add(cmd)
    }
    fun addPrivateCommand(cmd: String) {
        privateCommands.add(cmd)
    }
    fun addDashboardCommand(cmd: String) {
        dashboardCommands.add(cmd)
    }


    fun startsWithPrefix(message: String, guildPrefix: String): Int {
        if (message.isNotEmpty()) {
            if (message.startsWith(defaultPrefix)) {

                return defaultPrefix.length

            } else if (message.startsWith(guildPrefix)) {

                return guildPrefix.length

            }
        }
        return -1
    }

    /**
     * Returns 0 if valid
     *
     * Returns 1 if Command not in allowed channel
     *
     * Returns 2 if Command should be played in Dashboard if Guild has one
     */
    fun validateCommand(type: CommandType, command: String, message: Message): Int {
        if (type == CommandType.NORMAL && dashboardCommands.contains(command)) {
            val dGuild = ChannelUtils.getDGuildByMessage(message)
            val hasDashboard = dGuild.musicManager.hasDashboard()

            if (hasDashboard) return 2
        }

        return when (type) {
            CommandType.NORMAL -> if(nonSpecificCommands.contains(command)) 0 else 1
            CommandType.BOTCHANNEL -> if(botChannelCommands.contains(command)) 0 else 1
            CommandType.PRIVATE -> if(privateCommands.contains(command)) 0 else 1
            CommandType.DASHBOARD -> if(dashboardCommands.contains(command)) 0 else 1
        }
    }
    fun isCommand(command: String): Boolean {
        return CommandManager.getCommandList().contains(command)
    }
}