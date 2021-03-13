package eu.dragoncoding.dragonbot.commands.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.managers.GuildManager
import eu.dragoncoding.dragonbot.managers.music.AudioPlayerSearchResult
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.MusicUtils
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.VoiceChannel

class Play: Command {
    override val cmdLength: Int = "play".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild: DGuild = GuildManager.getGuild(message.guild.idLong)
        val args = getArgs(message, subString + cmdLength)

        if (args.isNotEmpty()) {

            val voiceChannel: VoiceChannel? = MusicUtils.getVoiceChannelFromMember(message.member)

            if (voiceChannel != null) {
                val apm: AudioPlayerManager = Bot.audioPlayerManager
                val manager = voiceChannel.guild.audioManager
                manager.openAudioConnection(voiceChannel)

                val strBuilder = StringBuilder()
                for (string in args) strBuilder.append( string ).append(" ")

                var url = strBuilder.toString().trim { it <= ' ' }

                if (!url.startsWith("http")) {
                    url = "ytsearch: $url"
                }

                dGuild.channels.tempChannelID1 = message.channel.idLong
                apm.loadItem(url, AudioPlayerSearchResult(dGuild.guildID))
            } else {
                errorCode = 2 //User not in a VoiceChannel
            }
        } else {
            errorCode = 1 //No argument
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }
}