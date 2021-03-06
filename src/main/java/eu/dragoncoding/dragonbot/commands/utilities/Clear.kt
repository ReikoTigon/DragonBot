package eu.dragoncoding.dragonbot.commands.utilities

import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.utils.ChatUtils.sendResponseToDiscord
import eu.dragoncoding.dragonbot.utils.SettingUtils
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.TextChannel
import java.util.*

class Clear: Command {

    override val cmdLength: Int = "clear".length + 1
    override var errorCode: Int = 0

    var amount = 0

    override fun performCommand(message: Message, subString: Int) {
        if (message.member!!.hasPermission(message.textChannel, Permission.MESSAGE_MANAGE)) {
            val args = getArgs(message, subString + cmdLength)
            if (args.isNotEmpty()) {
                try {
                    amount = if (args.isNotEmpty()) { args[0].toInt() } else { 1 }
                    message.channel.purgeMessages(getMessagesToPurge(message.channel, amount + 1, SettingUtils.checkDeleteCmd(message.guild.idLong)))
                } catch (e: NumberFormatException) {
                    errorCode = 2
                }
            }
        } else {
            errorCode = 1
        }

    }

    override fun response(channel: TextChannel) {
        val responseMessages = ArrayList<String>()

        responseMessages.add("Purged " + amount + if (amount == 1) " message!" else " messages!")
        responseMessages.add("Missing Permission! (Manage Messages)")
        responseMessages.add("The amount of messages to delete is invalid. Please only use numbers.")

        sendResponseToDiscord(errorCode, channel, responseMessages)
    }

    fun getMessagesToPurge(channel: MessageChannel, amount: Int, delCmd: Boolean): List<Message> {
        val messages: MutableList<Message> = ArrayList()
        var i = amount
        for (message in channel.iterableHistory.cache(false)) {
            if (!delCmd && i == (amount + 1)) {
                i--
                continue
            }

            if (!message.isPinned) {
                messages.add(message)
            }
            --i
            if (i <= 0) break
        }
        return messages
    }
}