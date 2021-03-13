package eu.dragoncoding.dragonbot.managers.music

import eu.dragoncoding.dragonbot.dashboardReactions
import eu.dragoncoding.dragonbot.defaultChannelID
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.AudioState
import eu.dragoncoding.dragonbot.structures.PlayType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import eu.dragoncoding.dragonbot.utils.DashboardUtils
import eu.dragoncoding.dragonbot.utils.TimeUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class MusicDashboard(dGuild: DGuild) {

    private val musicController: GuildMusicController = dGuild.musicManager

    val dGuild: DGuild
        get() = musicController.dGuild

    val dashboardChannel: TextChannel?
        get() = ChannelUtils.getTextChannelFromDGuild(dGuild, musicController.dGuild.channels.dashboardChannelID)

    private val messages: ArrayList<Message> = ArrayList()
    var initialPost: Boolean = true

    fun setIdle() {
        if (verifyDashboard()) {

            DashboardUtils.removeMessages(dashboardChannel!!)
            messages.clear()

            ChatUtils.sendEmbed("✅ BOT ONLINE", "No music playing. \nUse ${dGuild.prefix}play to start a song.", dashboardChannel!!, 0L, null)

            initialPost = true

        }
    }

    fun setActive() {
        if (verifyDashboard()) {

            DashboardUtils.removeMessages(dashboardChannel!!)
            messages.clear()

            updateQueue()
            updateNowPlaying()
            updateVolume()

            initialPost = false

        }
    }

    fun updateQueue() {
        if (verifyDashboard()) {

            val queueList = musicController.queueHandler.getQueue()

            val builder = EmbedBuilder()
            builder.setTitle("**The next ${if (queueList.size < 5) queueList.size else 5} songs:**")
            builder.setFooter("Next ${if (queueList.size < 5) queueList.size else 5} of ${queueList.size} \n---------------------------------------------------------------------------------------------------------------")

            for ((i, track) in queueList.withIndex()) {
                val info = track.info
                builder.addField("$i: ${info.author}", "${ ChatUtils.textToUrlText(info.title,info.uri) }\nLength: ${TimeUtils.longToTimeStringS(info.length)}", false)

                if (i > 4) break
            }

            if (initialPost) {

                val message: Message = dashboardChannel!!.sendMessage(builder.build()).complete()

                message.addReaction(dashboardReactions[10]).queue()

                messages.add(message)

            } else {

                messages[0].editMessage(builder.build()).complete()

            }

        }
    }
    fun updateNowPlaying() {
        if (verifyDashboard()) {
            val track = musicController.audioPlayer.playingTrack
            val info = track.info
            val currTime = TimeUtils.longToTimeStringS(track.position)
            val maxTime = TimeUtils.longToTimeStringS(track.duration)

            val builder = EmbedBuilder()
            builder.setAuthor(info.author)
            builder.setTitle(info.title, info.uri)

            builder.setDescription("${if (info.isStream) "Live (Stream)" else "$currTime / $maxTime"}\n" +
                                   "State: ${if (musicController.state == AudioState.PAUSED) "paused" else "playing"}\n" +
                                   "Loop: ${if (musicController.playType == PlayType.LOOPSINGLE) "Single Song" else if (musicController.playType == PlayType.LOOPALL) "All" else "None"}"
            )

            builder.setFooter("---------------------------------------------------------------------------------------------------------------")

            if (initialPost) {

                //val
                val message: Message = dashboardChannel!!.sendMessage(builder.build()).complete()

                message.addReaction(dashboardReactions[3]).queue()
                message.addReaction(dashboardReactions[4]).queue()
                message.addReaction(dashboardReactions[5]).queue()
                message.addReaction(dashboardReactions[6]).queue()
                message.addReaction(dashboardReactions[7]).queue()
                message.addReaction(dashboardReactions[8]).queue()
                message.addReaction(dashboardReactions[9]).queue()

                messages.add(message)

            } else {

                messages[1].editMessage(builder.build()).complete()

            }
        }
    }
    fun updateVolume() {
        val builder = EmbedBuilder()

        builder.setTitle("**Volume:**")
        builder.setDescription(musicController.audioPlayer.volume.toString())
        builder.setFooter("---------------------------------------------------------------------------------------------------------------")

        if (initialPost) {

            val message: Message = dashboardChannel!!.sendMessage(builder.build()).complete()

            message.addReaction(dashboardReactions[0]).queue()
            message.addReaction(dashboardReactions[1]).queue()
            message.addReaction(dashboardReactions[2]).queue()

            messages.add(message)

        } else {

            messages[2].editMessage(builder.build()).complete()

        }
    }

    private fun verifyDashboard(): Boolean {
        return if (DashboardUtils.checkIfDashboardExists(dGuild)) {
            true
        } else {
            removeDashboard()
            false
        }
    }
    private fun removeDashboard() {
        musicController.dashboard = null
        musicController.dGuild.channels.dashboardChannelID = defaultChannelID
    }
}