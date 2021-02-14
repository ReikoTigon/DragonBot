package eu.dragoncoding.dragonbot.listeners

import com.sun.istack.NotNull
import eu.dragoncoding.dragonbot.*
import eu.dragoncoding.dragonbot.managers.GuildManager.getGuild
import eu.dragoncoding.dragonbot.Bot.shardMan
import eu.dragoncoding.dragonbot.utils.ChatUtils.sendEmbed
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.Category
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent

class GuildListener : ListenerAdapter() {

    override fun onGuildJoin(@NotNull event: GuildJoinEvent) {
        val dGuild = getGuild(event.guild.idLong)
        dGuild.activate()

        val guild = shardMan.getGuildById(dGuild.guildID)

        guild?.createCategory("DragonBot")?.queue { category: Category ->
            category.createTextChannel("BotChannel")
                .setTopic(botChannelTopic)
                .queue { textChannel: TextChannel ->
                    dGuild.botChannelID = textChannel.idLong
                    dGuild.update()

                    sendEmbed("Hello! :D", botHelloMessage, textChannel, 0L, colorDefault)
                    sendEmbed("About this Channel", botChannelInfo, textChannel, 0L, colorDefault)
                }
        }

    }

    override fun onGuildLeave(@NotNull event: GuildLeaveEvent) {
        val dGuild = getGuild(event.guild.idLong)
        dGuild.deactivate()
    }
}