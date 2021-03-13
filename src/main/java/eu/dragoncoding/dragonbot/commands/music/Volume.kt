package eu.dragoncoding.dragonbot.commands.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import eu.dragoncoding.dragonbot.utils.MusicUtils
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class Volume: Command {
    override val cmdLength: Int = "volume".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild: DGuild = ChannelUtils.getDGuildByMessage(message)
        val player: AudioPlayer = dGuild.musicManager.audioPlayer

        val args = getArgs(message, subString + cmdLength)

        errorCode = MusicUtils.isInMusicVoiceChannel(message.member, player)
        if (errorCode == 0) {
            dGuild.channels.tempChannelID1 = message.channel.idLong

            if (args.isNotEmpty()) {
                val amount: Int = if (args.isNotEmpty()) { args[0].toInt() } else { player.volume }

                when {
                    amount <= 0 -> {
                        setVolume(0, dGuild, message.textChannel, player)
                    }
                    amount > 100 -> {
                        setVolume(100, dGuild, message.textChannel, player)
                    }
                    else -> {
                        setVolume(amount, dGuild, message.textChannel, player)
                    }
                }
            } else {
                errorCode = 4 //No volume set
            }
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }

    private fun setVolume(volume: Int, dGuild: DGuild, textChannel: TextChannel, player: AudioPlayer) {
        player.volume = volume
        Bot.shardMan.getGuildById(dGuild.guildID)!!.selfMember.mute(volume == 0).queue()

        if (dGuild.musicManager.hasDashboard()) {
            dGuild.musicManager.dashboard!!.updateVolume()
        } else {
            ChatUtils.sendEmbed("Volume", volume.toString(), textChannel, 5L, null)
        }
    }

    companion object {
        fun setVolume(volume: Int, dGuild: DGuild) {
            dGuild.musicManager.audioPlayer.volume = volume
            Bot.shardMan.getGuildById(dGuild.guildID)!!.selfMember.mute(volume == 0).queue()

            if (dGuild.musicManager.hasDashboard()) {
                dGuild.musicManager.dashboard!!.updateVolume()
            }
        }
    }
}