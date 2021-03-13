package eu.dragoncoding.dragonbot.commands.management

import eu.dragoncoding.dragonbot.*
import eu.dragoncoding.dragonbot.commands.utilities.Clear
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class BotChannel: Command {
    override val cmdLength: Int = "botchannel".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild = ChannelUtils.getDGuildByMessage(message)
        val args = getArgs(message, subString + cmdLength)

        when (args.size) {
            0 -> {
                sendBotChannelInfo(dGuild, message.textChannel)
            }
            1 -> {
                if (args[0] == "setup") {
                    if (!hasBotChannel(dGuild)) {
                        createBotChannel(dGuild)
                    }
                    sendBotChannelInfo(dGuild, message.textChannel)
                } else {
                    errorCode = 1 //Invalid argument
                }
            }
            else -> {
                errorCode = 2 //Invalid amount of arguments
            }
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }

    private fun hasBotChannel(dGuild: DGuild): Boolean {
        val channelID = dGuild.channels.botChannelID
        val channel = ChannelUtils.getTextChannelFromDGuild(dGuild, channelID)

        return channel != null
    }
    private fun createBotChannel(dGuild: DGuild) {
        val guild = Bot.shardMan.getGuildById(dGuild.guildID)!!

        guild.createTextChannel("BotChannel")
                .setTopic(botChannelTopic)
                .queue { textChannel: TextChannel ->
                    dGuild.channels.botChannelID = textChannel.idLong
                    dGuild.update()

                    ChatUtils.sendEmbed("Hello! :D", botHelloMessage, textChannel, 0L, colorDefault)
                    ChatUtils.sendEmbed("About this Channel", botChannelInfo, textChannel, 0L, colorDefault)

                    textChannel.iterableHistory.forEach {
                        it.pin().queue()
                    }

                    val clear =  Clear()
                    textChannel.purgeMessages(clear.getMessagesToPurge(textChannel, 2, true))
                }
    }
    private fun sendBotChannelInfo(dGuild: DGuild, sendTo: TextChannel) {
        val builder = EmbedBuilder()
        builder.setTitle("The MusicChannel is")

        val channel: String
        val temp = ChannelUtils.getTextChannelFromDGuild(dGuild, dGuild.channels.botChannelID)
        channel = temp?.asMention ?: "N/A"

        builder.setDescription(channel)

        ChatUtils.sendEmbed(builder, sendTo, 0L, null)
    }
}