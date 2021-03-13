package eu.dragoncoding.dragonbot.commands.management

import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class SNowPlaying: Command {
    override val cmdLength: Int = "nowplaying".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild = ChannelUtils.getDGuildByMessage(message)
        val args = getArgs(message, subString + cmdLength)

        when (args.size) {
            0 -> {
                sendNowPlayingInfo(dGuild, message.textChannel)
            }
            1 -> {
                when (args[0]) {
                    "activate" -> {
                        if (!dGuild.musicManager.hasDashboard()) {

                            dGuild.settings.showNowPlaying = true
                            dGuild.update()

                        } else {
                            errorCode = 3 //Can't activate when MusicDashboard is active
                        }
                    }
                    "deactivate" -> {
                        dGuild.settings.showNowPlaying = false
                        dGuild.update()
                    }
                    else -> {
                        errorCode = 2 //Unknown Command addon
                    }
                }
                sendNowPlayingInfo(dGuild, message.textChannel)
            }
            else -> {
                errorCode = 1 //Invalid argument length
            }
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }

    private fun sendNowPlayingInfo(dGuild: DGuild, sendTo: TextChannel) {
        val builder = EmbedBuilder()

        val settings = dGuild.settings.showNowPlaying

        builder.setDescription("NowPlaying is ${if (settings) "activated" else "deactivated"}")

        ChatUtils.sendEmbed(builder, sendTo, 0L, null)
    }
}