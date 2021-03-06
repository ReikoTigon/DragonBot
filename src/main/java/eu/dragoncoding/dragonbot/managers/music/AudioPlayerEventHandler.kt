package eu.dragoncoding.dragonbot.managers.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.managers.GuildManager
import eu.dragoncoding.dragonbot.utils.ChatUtils
import eu.dragoncoding.dragonbot.utils.SettingUtils
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel

class AudioPlayerEventHandler(player: AudioPlayer) : AudioEventAdapter() {

    private val musicController: GuildMusicController


    override fun onPlayerPause(player: AudioPlayer) {    }
    override fun onPlayerResume(player: AudioPlayer) {    }

    override fun onTrackStart(player: AudioPlayer, track: AudioTrack) {
        val guildId = GuildManager.getGuildByAudioPlayerHash(player.hashCode())
        val dGuild: DGuild = GuildManager.getGuild(guildId)

        if (SettingUtils.checkNowPlaying(dGuild)) {
            val guild: Guild = Bot.shardMan.getGuildById(dGuild.guildID)!!
            var channel: TextChannel? = guild.getTextChannelById(dGuild.channels.musicChannelID)

            if (channel == null) {
                channel = guild.getTextChannelById(dGuild.channels.tempChannelID_1)!!
            }

            ChatUtils.sendEmbed("Now Playing:", ChatUtils.textToUrlText(track.info.title, track.info.uri), channel, 0L, null)
        }
    }
    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext && endReason != AudioTrackEndReason.REPLACED) {
            musicController.queueHandler.playNext(false)
        }
    }

    init {
        val guildId = GuildManager.getGuildByAudioPlayerHash(player.hashCode())
        musicController = GuildManager.getGuild(guildId).musicManager
    }
}