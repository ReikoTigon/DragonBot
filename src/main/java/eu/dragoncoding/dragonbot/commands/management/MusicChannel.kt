package eu.dragoncoding.dragonbot.commands.management

import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class MusicChannel: Command {
    override val cmdLength: Int = "musicchannel".length + 1
    override var errorCode: Int = 0

    //-musicChannel
    //-musicChannel set
    //-musicChannel set #Channel

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild = ChannelUtils.getDGuildByMessage(message)
        val args = getArgs(message, subString + cmdLength)

        when (args.size) {
            0 -> {
                sendMusicChannelInfo(dGuild, message.textChannel)
            }
            1 -> {
                if (args[0] == "set") {
                    setMusicChannelInfo(dGuild, message.textChannel)
                    sendMusicChannelInfo(dGuild, message.textChannel)
                } else {
                    errorCode = 1 //Invalid argument
                }
            }
            2 -> {
                if (args[0] == "set") {
                    val textChannels = message.mentionedChannels
                    if (textChannels.isNotEmpty()) {
                        setMusicChannelInfo(dGuild, textChannels[0])
                        sendMusicChannelInfo(dGuild, message.textChannel)
                    } else {
                        errorCode = 3 //No Channel Mentioned
                    }
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

    private fun setMusicChannelInfo(dGuild: DGuild, setTo: TextChannel) {
        dGuild.channels.musicChannelID = setTo.idLong
        dGuild.update()
    }
    private fun sendMusicChannelInfo(dGuild: DGuild, sendTo: TextChannel) {
        val builder = EmbedBuilder()
        builder.setTitle("The MusicChannel is")

        val channel: String
        var temp = ChannelUtils.getTextChannelFromDGuild(dGuild, dGuild.channels.musicChannelID)
        if (temp != null) {
            channel = temp.asMention
        } else {
            temp = ChannelUtils.getTextChannelFromDGuild(dGuild, dGuild.channels.tempChannelID1)
            channel = temp?.asMention ?: "N/A"
        }

        builder.setDescription(channel)

        ChatUtils.sendEmbed(builder, sendTo, 0L, null)
    }
}