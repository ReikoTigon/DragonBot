package eu.dragoncoding.dragonbot.commands.management

import eu.dragoncoding.dragonbot.defaultChannelID
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.managers.music.MusicDashboard
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class Dashboard: Command {
    override val cmdLength: Int = "dashboard".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild = ChannelUtils.getDGuildByMessage(message)
        val args = getArgs(message, subString + cmdLength)

        if (args.isEmpty() || args[0].isEmpty()) {

            sendDashboardInfo(dGuild, message.textChannel)

        } else if (args.size == 1) { //dashboard activate/deactivate

            when (args[0]) {
                "activate" -> {
                    val category = ChannelUtils.getTextChannelFromDGuild(dGuild, dGuild.channels.botChannelID)?.parent

                    val dashboardChannel: TextChannel = if (category != null) {
                        category.createTextChannel("Music-Dashboard").complete()
                    } else {
                        message.guild.createTextChannel("Music-Dashboard").complete()
                    }

                    dGuild.settings.showNowPlaying = false
                    dGuild.settings.musicDashboard = true
                    dGuild.channels.dashboardChannelID = dashboardChannel.idLong

                    dGuild.musicManager.dashboard = MusicDashboard(dGuild)

                    dGuild.update()

                    if (dGuild.musicManager.hasDashboard()) {
                        dGuild.musicManager.dashboard!!.setIdle()
                    }

                    sendDashboardInfo(dGuild, message.textChannel)
                }
                "deactivate" -> {

                    if (dGuild.musicManager.hasDashboard()) {
                        dGuild.musicManager.dashboard!!.dashboardChannel?.delete()?.complete()

                        dGuild.settings.musicDashboard = false
                        dGuild.channels.dashboardChannelID = defaultChannelID

                        dGuild.update()

                        dGuild.musicManager.dashboard = null
                    }

                    sendDashboardInfo(dGuild, message.textChannel)
                }
                else -> {

                    errorCode = 1 //Unknown Dashboard-Command

                }
            }

        } else {

            errorCode = 2 //Invalid amount of arguments

        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }

    private fun sendDashboardInfo(dGuild: DGuild, sendTo: TextChannel) {
        val builder = EmbedBuilder()
        builder.setTitle("The Dashboard-Channel is")

        val channel: String
        val temp = ChannelUtils.getTextChannelFromDGuild(dGuild, dGuild.channels.dashboardChannelID)
        channel = temp?.asMention ?: "N/A"

        builder.setDescription(channel)

        ChatUtils.sendEmbed(builder, sendTo, 0L, null)
    }
}