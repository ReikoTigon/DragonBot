package eu.dragoncoding.dragonbot.listeners

import eu.dragoncoding.dragonbot.managers.CommandExecutor
import eu.dragoncoding.dragonbot.managers.CommandExecutor.performGuildCmd
import eu.dragoncoding.dragonbot.managers.CommandExecutor.performPrivateCmd
import eu.dragoncoding.dragonbot.managers.GuildManager.getGuild
import eu.dragoncoding.dragonbot.structures.CommandType
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class TextListener : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        onGuildMessage(event.guild.idLong, event.message)
    }

    override fun onGuildMessageUpdate(event: GuildMessageUpdateEvent) {
        onGuildMessage(event.guild.idLong, event.message)
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        onPrivateMessage(event.message)
    }

    override fun onPrivateMessageUpdate(event: PrivateMessageUpdateEvent) {
        onPrivateMessage(event.message)
    }

    private fun onGuildMessage(guild_ID: Long, message: Message) {
        val dGuild = getGuild(guild_ID)

        if (message.textChannel.idLong == dGuild.channels.dashboardChannelID) {
            if (message.member!!.idLong != message.guild.selfMember.idLong) {
                if (isNoCommandOrInvalid(message, dGuild.prefix)) {
                    message.delete().queue()
                    return
                }
            }
        }

        val cmdType = when (message.textChannel.idLong) {
            dGuild.channels.botChannelID -> CommandType.BOTCHANNEL
            dGuild.channels.dashboardChannelID -> CommandType.DASHBOARD
            else -> CommandType.NORMAL
        }

        performGuildCmd(dGuild, message, cmdType)
    }

    private fun onPrivateMessage(message: Message) {
        performPrivateCmd(message)
    }
    private fun isNoCommandOrInvalid(message: Message, prefix: String): Boolean {
        val prefix = CommandExecutor.startsWithPrefix(message.contentDisplay, prefix)

        if (prefix != -1) {
            if (message.contentDisplay.length > prefix) {

                val command = message.contentDisplay.substring(prefix).split(" ".toRegex())[0].toLowerCase()

                if (CommandExecutor.isCommand(command)) {
                    if (CommandExecutor.validateCommand(CommandType.DASHBOARD, command, message) == 0) {
                        return false
                    }
                }
            }
        }

        return true
    }
}