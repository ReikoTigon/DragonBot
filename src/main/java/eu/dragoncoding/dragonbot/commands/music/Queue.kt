package eu.dragoncoding.dragonbot.commands.music

import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import eu.dragoncoding.dragonbot.utils.SettingUtils
import eu.dragoncoding.dragonbot.utils.TimeUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class Queue: Command {
    override val cmdLength: Int = "queue".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild: DGuild = ChannelUtils.getDGuildByMessage(message)
        val queue = dGuild.musicManager.queueHandler.getQueue()

        val builder = EmbedBuilder()
        builder.setTitle("Song-Queue")

        for ((i, track) in queue.withIndex()) {

            if (i >= 10) {
                break
            }

            val info = track.info
            val title = "$i: ${info.author}"
            val description = "${ChatUtils.textToUrlText(info.title, info.uri)}\nLength: ${TimeUtils.longToTimeStringS(info.length)}"

            builder.addField(title, description, false)
        }


        dGuild.channels.tempChannelID1 = message.textChannel.idLong

        if (SettingUtils.checkNowPlaying(dGuild)) {
            val guild: Guild = Bot.shardMan.getGuildById(dGuild.guildID)!!
            var channel: TextChannel? = guild.getTextChannelById(dGuild.channels.musicChannelID)

            if (channel == null) {
                channel = guild.getTextChannelById(dGuild.channels.tempChannelID1)!!
            }

            ChatUtils.sendEmbed(builder, channel, 0L, null)
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }
}