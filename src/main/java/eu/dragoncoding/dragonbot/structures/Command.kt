@file:Suppress("LiftReturnOrAssignment", "LiftReturnOrAssignment")

package eu.dragoncoding.dragonbot.structures

import eu.dragoncoding.dragonbot.utils.SettingUtils
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

interface Command {

    val cmdLength: Int
    var errorCode: Int
    fun performCommand(message: Message, subString: Int, type: CommandType)
    fun response(channel: TextChannel)

    fun removeMessageIfActivated(message: Message) {
        if (SettingUtils.checkDeleteCmd(message.guild.idLong)) {
            message.delete().queue()
        }
    }

    fun getArgs(message: Message, subString: Int): List<String> {
        return if (message.contentRaw.length < subString) {
            message.contentRaw.substring(subString - 1).split(" ".toRegex())
        } else {
            message.contentRaw.substring(subString).split(" ".toRegex())
        }


    }
}