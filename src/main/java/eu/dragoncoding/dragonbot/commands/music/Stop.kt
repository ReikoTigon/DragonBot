package eu.dragoncoding.dragonbot.commands.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.MusicUtils
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class Stop: Command {
    override val cmdLength: Int = "stop".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild: DGuild = ChannelUtils.getDGuildByMessage(message)
        val manager = Bot.shardMan.getGuildById(dGuild.guildID)!!.audioManager
        val player: AudioPlayer = dGuild.musicManager.audioPlayer

        errorCode = MusicUtils.isInMusicVoiceChannel(message.member, player)
        if (errorCode == 0) {
            dGuild.channels.tempChannelID1 = message.channel.idLong

            dGuild.musicManager.queueHandler.clear()

            player.stopTrack()
            player.isPaused = false

            manager.closeAudioConnection()
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }
}