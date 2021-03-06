package eu.dragoncoding.dragonbot.commands.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.AudioState
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import eu.dragoncoding.dragonbot.utils.MusicUtils
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class Pause: Command {
    override val cmdLength: Int = "pause".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int) {
        removeMessageIfActivated(message)

        val dGuild: DGuild = ChannelUtils.getDGuildByMessage(message)
        val player: AudioPlayer = dGuild.musicManager.audioPlayer

        errorCode = MusicUtils.isInMusicVoiceChannel(message.member, player)
        if (errorCode == 0) {
            dGuild.channels.tempChannelID_1 = message.channel.idLong

            player.isPaused = !player.isPaused

            if (player.isPaused) {
                dGuild.musicManager.state = AudioState.PAUSED
                ChatUtils.sendEmbed("Music Paused", null, message.textChannel, 0L, null)
            } else {
                dGuild.musicManager.state = AudioState.PLAYING
                ChatUtils.sendEmbed("Music Resumed", null, message.textChannel, 5L, null)
            }
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }
}