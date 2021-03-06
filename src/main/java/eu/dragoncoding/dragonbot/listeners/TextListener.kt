package eu.dragoncoding.dragonbot.listeners

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
        val guild = getGuild(guild_ID)
        val isBotChannel = message.textChannel.idLong == guild.channels.botChannelID
        performGuildCmd(guild, message, if (isBotChannel) CommandType.BOTCHANNEL else CommandType.NORMAL)
    }

    private fun onPrivateMessage(message: Message) {
        performPrivateCmd(message)
    }
}