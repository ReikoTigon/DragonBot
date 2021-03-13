package eu.dragoncoding.dragonbot.commands.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.MusicUtils
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class Skip: Command {
    override val cmdLength: Int = "skip".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild: DGuild = ChannelUtils.getDGuildByMessage(message)
        val player: AudioPlayer = dGuild.musicManager.audioPlayer

        errorCode = MusicUtils.isInMusicVoiceChannel(message.member, player) //returns 0, 1, 2, 3

        if (errorCode == 0) {
            dGuild.channels.tempChannelID1 = message.channel.idLong

            if(!dGuild.musicManager.queueHandler.playNext(true)) {
                errorCode = 4 //Empty Queue
            }
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }
}