package eu.dragoncoding.dragonbot.listeners

import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.commands.music.Volume
import eu.dragoncoding.dragonbot.dashboardReactions
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.managers.GuildManager
import eu.dragoncoding.dragonbot.providedBy
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ReactionListener: ListenerAdapter() {

    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        if (event.member.idLong != event.guild.selfMember.idLong) {
            val dGuild: DGuild = GuildManager.getGuild(event.guild.idLong)
            val reactionAsString = event.reactionEmote.asReactionCode

            if (event.channel.idLong == dGuild.channels.dashboardChannelID) {

                if (dashboardReactions.contains(reactionAsString)) {
                    val index = dashboardReactions.indexOf(reactionAsString)

                    execute(dGuild, index, event.member)
                }

                event.channel.retrieveMessageById(event.reaction.messageIdLong).queue({
                    event.reaction.removeReaction(event.member.user).queue()
                }, { })
            }
        }
    }


    private fun execute(dGuild: DGuild, index: Int, member: Member) {
        when (index) {
            0 -> {
                if ((dGuild.musicManager.audioPlayer.volume -5) >= 0) {
                    Volume.setVolume(dGuild.musicManager.audioPlayer.volume - 5, dGuild)
                }
            }
            1 -> {
                if ((dGuild.musicManager.audioPlayer.volume +5) <= 100) {
                    Volume.setVolume(dGuild.musicManager.audioPlayer.volume + 5, dGuild)
                }
            }
            2 -> {
                if ((dGuild.musicManager.audioPlayer.volume +5) <= 100) {
                    Volume.setVolume(10, dGuild)
                }
            }
            3 -> {
                dGuild.musicManager.queueHandler.shuffle()
                dGuild.musicManager.dashboard!!.updateQueue()
            }
            4 -> {
                dGuild.musicManager.queueHandler.clear()

                dGuild.musicManager.audioPlayer.stopTrack()
                dGuild.musicManager.audioPlayer.isPaused = false

                Bot.shardMan.getGuildById(dGuild.guildID)!!.audioManager.closeAudioConnection()
            }
            5 -> {
                dGuild.musicManager.queueHandler.playLast()
                dGuild.musicManager.dashboard!!.updateQueue()
            }
            6 -> {
                dGuild.musicManager.audioPlayer.isPaused = !dGuild.musicManager.audioPlayer.isPaused
                //ToDO Maybe update now-playing to show paused
            }
            7 -> {
                dGuild.musicManager.queueHandler.playNext(true)
                dGuild.musicManager.dashboard!!.updateQueue()
            }
            8 -> {
                //ToDo Set loop all
            }
            9 -> {
                //ToDo Set single loop all
            }
            10 -> {
                help(member)
            }
            else -> {}
        }
    }

    private fun help(member: Member) {
        val builder = EmbedBuilder()
        builder.setTitle("**Dashboard-Help: **")
        builder.setFooter(providedBy)

        builder.addField("❓", "Help comes.", true)

        builder.addField("", "", false)

        builder.addField("🔀", "Shuffles the songs.", true)
        builder.addField("⏹️", "Stops the Music bot.", true)
        builder.addField("⏮️", "Goes one song back.", true)
        builder.addField("⏸️", "Pauses/Resumes the song.", true)
        builder.addField("⏭️", "Next Song.", true)
        builder.addField("🔁", "Loops over all songs.", true)
        builder.addField("🔂", "Loops a single song.", true)

        builder.addField("", "", false)

        builder.addField("🔉", "Please...be quieter.", true)
        builder.addField("🔊", "Louder... mix it up.", true)
        builder.addField("🔄", "Resets the volume to 10.", true)

        val channel = member.user.openPrivateChannel().complete()
        channel.sendMessage(builder.build()).queue()
    }
}