package eu.dragoncoding.dragonbot.utils

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.colorError
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.managers.GuildManager
import net.dv8tion.jda.api.entities.*
import java.util.*

object MusicUtils {
    /**
     * Checks if the AudioPlayer and a member are in the same channel.
     *
     * @param[member] The member to check.
     * @param[audioPlayer] The AudioPlayer to check.
     *
     * @return An Int:
     *         0(Are connected to the same),
     *         1(Member in no channel),
     *         2(Bot in no Channel),
     *         3(Not in the same Channel)
     */
    fun isInMusicVoiceChannel(member: Member?, audioPlayer: AudioPlayer): Int {
        val memberVoiceChannel = getVoiceChannelFromMember(member)
        val playerVoiceChannel = getVoiceChannelFromPlayer(audioPlayer)

        return if (memberVoiceChannel != null ) {
            if (playerVoiceChannel != null) {
                if (memberVoiceChannel.idLong == playerVoiceChannel.idLong) {
                    0 //Success
                } else {
                    3 //Not in the same Channel
                }
            } else {
                2 //Bot not in a VoiceChannel
            }
        } else {
            1 //Not Connected to VoiceChannel
        }
    }
    fun getVoiceChannelFromMember(member: Member?): VoiceChannel? {
        if (member != null) {
            if (member.voiceState != null) {
                if (member.voiceState!!.channel != null) {
                    return member.voiceState!!.channel
                }
            }
        }

        return null
    }
    fun getVoiceChannelFromPlayer(audioPlayer: AudioPlayer): VoiceChannel? {
        val dGuild = GuildManager.getGuild(GuildManager.getGuildByAudioPlayerHash(audioPlayer.hashCode()))
        return Bot.shardMan.getGuildById(dGuild.guildID)!!.audioManager.connectedChannel
    }
}

object SettingUtils {
    fun checkNowPlaying(dGuild: DGuild): Boolean {
        return dGuild.settings.showNowPlaying
    }
    fun checkNowPlaying(guildID: Long): Boolean {
        return GuildManager.getGuild(guildID).settings.showNowPlaying
    }

    fun checkDeleteCmd(dGuild: DGuild): Boolean {
        return dGuild.settings.deleteCommands
    }
    fun checkDeleteCmd(guildID: Long): Boolean {
        return GuildManager.getGuild(guildID).settings.deleteCommands
    }

    fun checkMusicDashboard(dGuild: DGuild): Boolean {
        return dGuild.settings.musicDashboard
    }
    fun checkMusicDashboard(guildID: Long): Boolean {
        return GuildManager.getGuild(guildID).settings.musicDashboard
    }
}

object ChannelUtils {
    fun getTextChannelFromDGuild(dGuild: DGuild, channel_ID: Long): TextChannel? {
        val guild: Guild = Bot.shardMan.getGuildById(dGuild.guildID)!!

        return guild.getTextChannelById(channel_ID)
    }

    fun getDGuildByMessage(message: Message): DGuild {
        return GuildManager.getGuild(message.guild.idLong)
    }
}

object DashboardUtils {
    fun onStartUp() {
        for (guild in Bot.shardMan.guilds) {
            val dGuild = GuildManager.getGuild(guild.idLong)

            if (dGuild.musicManager.hasDashboard()) {
                dGuild.musicManager.dashboard!!.setIdle()
            }
        }
    }
    fun onShutDown() {
        for (guild in Bot.shardMan.guilds) {
            val dGuild = GuildManager.getGuild(guild.idLong)

            if (dGuild.musicManager.hasDashboard()) {
                val channel = dGuild.musicManager.dashboard!!.dashboardChannel

                if (channel != null) {

                    removeMessages(channel)

                    ChatUtils.sendEmbed("🔴 BOT offline", "Sorry, we're trying to change this state. \n...but the squirrels keep munching on our cables.", channel, 0L, colorError)
                }
            }
        }
    }

    fun checkIfDashboardExists(dGuild: DGuild): Boolean {
        val dashboard = dGuild.musicManager.dashboard
        if (dashboard != null) {
            val channel = dashboard.dashboardChannel

            if (channel != null) {
                return true
            }
        }

        return false
    }

    fun removeMessages(channel: TextChannel) {
        val messages: MutableList<Message> = ArrayList()

        for (message in channel.iterableHistory.cache(false)) {
            messages.add(message)
        }

        channel.purgeMessages(messages)
    }
}