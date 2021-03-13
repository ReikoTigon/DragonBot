package eu.dragoncoding.dragonbot.managers.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.managers.GuildManager
import eu.dragoncoding.dragonbot.structures.AudioState
import eu.dragoncoding.dragonbot.utils.ChatUtils
import eu.dragoncoding.dragonbot.utils.SettingUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import java.util.concurrent.TimeUnit

class AudioPlayerEventHandler(dGuild: DGuild) : AudioEventAdapter() {

    private val musicController: GuildMusicController = dGuild.musicManager
    private var pauseMessage: Message? = null

    override fun onPlayerPause(player: AudioPlayer) {
        val guildId = GuildManager.getGuildByAudioPlayerHash(player.hashCode())
        val dGuild: DGuild = GuildManager.getGuild(guildId)

        if (SettingUtils.checkNowPlaying(dGuild)) {

            val builder = EmbedBuilder()
            builder.setDescription("Music paused.")

            val guild: Guild = Bot.shardMan.getGuildById(dGuild.guildID)!!
            var channel: TextChannel? = guild.getTextChannelById(dGuild.channels.musicChannelID)

            if (channel == null) {
                channel = guild.getTextChannelById(dGuild.channels.tempChannelID1)!!
            }

            pauseMessage = channel.sendMessage(builder.build()).complete()

        }

        musicController.state = AudioState.PAUSED

        if (dGuild.musicManager.hasDashboard()) {
            dGuild.musicManager.dashboard!!.updateNowPlaying()
        }

    }
    override fun onPlayerResume(player: AudioPlayer) {
        val guildId = GuildManager.getGuildByAudioPlayerHash(player.hashCode())
        val dGuild: DGuild = GuildManager.getGuild(guildId)

        if (SettingUtils.checkNowPlaying(dGuild)) {

            val builder = EmbedBuilder()
            builder.setDescription("Music resumed.")

            val guild: Guild = Bot.shardMan.getGuildById(dGuild.guildID)!!
            var channel: TextChannel? = guild.getTextChannelById(dGuild.channels.musicChannelID)

            if (channel == null) {
                channel = guild.getTextChannelById(dGuild.channels.tempChannelID1)!!
            }

            pauseMessage?.delete()?.queue()

            channel.sendMessage(builder.build()).complete().delete().queueAfter(5, TimeUnit.SECONDS)

        }

        musicController.state = AudioState.PLAYING
    }

    override fun onTrackStart(player: AudioPlayer, track: AudioTrack) {
        val guildId = GuildManager.getGuildByAudioPlayerHash(player.hashCode())
        val dGuild: DGuild = GuildManager.getGuild(guildId)

        if (SettingUtils.checkNowPlaying(dGuild)) {
            val guild: Guild = Bot.shardMan.getGuildById(dGuild.guildID)!!
            var channel: TextChannel? = guild.getTextChannelById(dGuild.channels.musicChannelID)

            if (channel == null) {
                channel = guild.getTextChannelById(dGuild.channels.tempChannelID1)!!
            }

            ChatUtils.sendEmbed("Now Playing:", ChatUtils.textToUrlText(track.info.title, track.info.uri), channel, 0L, null)
        } else {
             if (musicController.hasDashboard() && musicController.dashboard!!.initialPost) {
                 musicController.dashboard!!.setActive()
             }
        }
    }
    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext) {
            musicController.queueHandler.playNext(false)
        } else if (endReason == AudioTrackEndReason.REPLACED) {
            return
        } else {
            musicController.state = AudioState.IDLE

            player.stopTrack()
            musicController.queueHandler.clear()
            Bot.shardMan.getGuildById(musicController.dGuild.guildID)!!.audioManager.closeAudioConnection()

            if (musicController.hasDashboard()) {
                musicController.dashboard!!.setIdle()
            }
        }
    }
}