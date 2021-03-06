package eu.dragoncoding.dragonbot.structures

import eu.dragoncoding.dragonbot.utils.SettingUtils
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

interface Command {

    val cmdLength: Int
    var errorCode: Int
    fun performCommand(message: Message, subString: Int)
    fun response(channel: TextChannel)

    fun removeMessageIfActivated(message: Message) {
        if (SettingUtils.checkDeleteCmd(message.guild.idLong)) {
            message.delete().queue()
        }
    }

    fun getArgs(message: Message, subString: Int): List<String> {
        return message.contentRaw.substring(subString).split(" ".toRegex())
    }
}