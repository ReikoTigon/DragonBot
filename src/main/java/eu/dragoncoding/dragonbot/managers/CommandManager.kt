package eu.dragoncoding.dragonbot.managers

import eu.dragoncoding.dragonbot.commands.Clear
import eu.dragoncoding.dragonbot.structures.Command
import net.dv8tion.jda.api.entities.Message
import java.util.concurrent.ConcurrentHashMap

object CommandManager {

    private val commands: ConcurrentHashMap<String, Command> = ConcurrentHashMap<String, Command>()

    fun perform(command: String, message: Message, subString: Int): Boolean {
        val serverCommand: Command
        if (commands[command.toLowerCase()] != null) {

            serverCommand = commands[command.toLowerCase()]!!
            serverCommand.performCommand(message, subString)
            return true
        }
        return false
    }

    init {
        this.commands["clear"] = Clear()
    }

    fun getCommandList(): List<String> {
        return commands.keys().toList()
    }
}