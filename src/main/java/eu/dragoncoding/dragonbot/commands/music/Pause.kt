package eu.dragoncoding.dragonbot.commands.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.MusicUtils
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class Pause: Command {
    override val cmdLength: Int = "pause".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild: DGuild = ChannelUtils.getDGuildByMessage(message)
        val player: AudioPlayer = dGuild.musicManager.audioPlayer

        errorCode = MusicUtils.isInMusicVoiceChannel(message.member, player)
        if (errorCode == 0) {
            dGuild.channels.tempChannelID1 = message.channel.idLong

            player.isPaused = !player.isPaused
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }
}