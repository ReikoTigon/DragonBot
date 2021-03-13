package eu.dragoncoding.dragonbot.commands.management

import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class SDeleteCommands: Command {
    override val cmdLength: Int = "deleteCommands".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val dGuild = ChannelUtils.getDGuildByMessage(message)
        val args = getArgs(message, subString + cmdLength)

        when (args.size) {
            0 -> {
                sendDeleteCommandsInfo(dGuild, message.textChannel)
            }
            1 -> {
                when (args[0]) {
                    "activate" -> {
                        dGuild.settings.deleteCommands = true
                        dGuild.update()
                    }
                    "deactivate" -> {
                        dGuild.settings.deleteCommands = false
                        dGuild.update()
                    }
                    else -> {
                        errorCode = 2 //Unknown Command addon
                    }
                }
                sendDeleteCommandsInfo(dGuild, message.textChannel)
            }
            else -> {
                errorCode = 1 //Invalid argument length
            }
        }
    }

    override fun response(channel: TextChannel) {
        TODO("Not yet implemented")
    }

    private fun sendDeleteCommandsInfo(dGuild: DGuild, sendTo: TextChannel) {
        val builder = EmbedBuilder()

        val settings = dGuild.settings.deleteCommands

        builder.setDescription("DeleteCommands is ${if (settings) "activated" else "deactivated"}")

        ChatUtils.sendEmbed(builder, sendTo, 0L, null)
    }
}