package eu.dragoncoding.dragonbot.listeners

import com.sun.istack.NotNull
import eu.dragoncoding.dragonbot.Bot.shardMan
import eu.dragoncoding.dragonbot.botChannelInfo
import eu.dragoncoding.dragonbot.botChannelTopic
import eu.dragoncoding.dragonbot.botHelloMessage
import eu.dragoncoding.dragonbot.colorDefault
import eu.dragoncoding.dragonbot.commands.utilities.Clear
import eu.dragoncoding.dragonbot.managers.GuildManager.getGuild
import eu.dragoncoding.dragonbot.utils.ChatUtils.sendEmbed
import net.dv8tion.jda.api.entities.Category
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GuildListener : ListenerAdapter() {

    override fun onGuildJoin(@NotNull event: GuildJoinEvent) {
        val dGuild = getGuild(event.guild.idLong)
        dGuild.activate()

        val guild = shardMan.getGuildById(dGuild.guildID)!!

        guild.createCategory("DragonBot").queue { category: Category ->
            category.createTextChannel("BotChannel")
                .setTopic(botChannelTopic)
                .queue { textChannel: TextChannel ->
                    dGuild.channels.botChannelID = textChannel.idLong
                    dGuild.update()

                    sendEmbed("Hello! :D", botHelloMessage, textChannel, 0L, colorDefault)
                    sendEmbed("About this Channel", botChannelInfo, textChannel, 0L, colorDefault)

                    textChannel.iterableHistory.forEach {
                        it.pin().queue()
                    }

                    val clear =  Clear()
                    textChannel.purgeMessages(clear.getMessagesToPurge(textChannel, 2, true))
                }
        }
    }

    override fun onGuildLeave(@NotNull event: GuildLeaveEvent) {
        val dGuild = getGuild(event.guild.idLong)
        dGuild.deactivate()
    }
}