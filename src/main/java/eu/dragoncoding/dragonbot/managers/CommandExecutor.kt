package eu.dragoncoding.dragonbot.managers

import eu.dragoncoding.dragonbot.defaultPrefix
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.CommandType
import net.dv8tion.jda.api.entities.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

object CommandExecutor {

    private val logger: Logger = LoggerFactory.getLogger(CommandExecutor::class.java)

    private val nonSpecificCommands: ArrayList<String> = ArrayList()
    private val botChannelCommands: ArrayList<String> = ArrayList()
    private val privateCommands: ArrayList<String> = ArrayList()

    fun performGuildCmd(dGuild: DGuild, msg: Message, type: CommandType) {
        val message = msg.contentDisplay
        val prefix = startsWithPrefix(message, dGuild.prefix)

        if (prefix != -1) {
            if (message.length > prefix) {

                val command = message.substring(prefix).split(" ".toRegex())[0].toLowerCase()

                if (isCommand(command)) {
                    if (validateCommand(type, command)) {

                        CommandManager.perform(command, msg, prefix)
                        dGuild.incrementCommands()
                    } else {
                        //ToDo ErrorMessage -> Command not allowed in this Channel
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
                    if (validateCommand(CommandType.PRIVATE, command)) {

                        CommandManager.perform(command, msg, prefix)
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


    private fun startsWithPrefix(message: String, guildPrefix: String): Int {
        if (message.isNotEmpty()) {
            if (message.startsWith(defaultPrefix)) {

                return defaultPrefix.length

            } else if (message.startsWith(guildPrefix)) {

                return guildPrefix.length

            }
        }
        return -1
    }
    private fun validateCommand(type: CommandType, command: String): Boolean {
        return when (type) {
            CommandType.NORMAL -> nonSpecificCommands.contains(command)
            CommandType.BOTCHANNEL -> botChannelCommands.contains(command)
            CommandType.PRIVATE -> privateCommands.contains(command)
            else -> { false }
        }
    }
    private fun isCommand(command: String): Boolean {
        return CommandManager.getCommandList().contains(command)
    }
}