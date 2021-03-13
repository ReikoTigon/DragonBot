package eu.dragoncoding.dragonbot.commands.utilities

import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.codingGroup
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.structures.CommandType
import eu.dragoncoding.dragonbot.utils.ChatUtils
import eu.dragoncoding.dragonbot.utils.JSONLoader
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.TextChannel

class BotInfo: Command {
    override val cmdLength: Int = "botinfo".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int, type: CommandType) {
        removeMessageIfActivated(message)

        val botName = Bot.shardMan.getGuildById(message.guild.idLong)!!.selfMember.effectiveName
        val ping: Long = getPing(message.channel)
        val author = "$codingGroup (${JSONLoader.contactDiscord})"
        val inviteLink = JSONLoader.inviteLink
        val supportDiscord = JSONLoader.discordLink
        val joinedGuilds = DGuild.getAllActive().size
        val totalCommandsUsed = commandCounter()

        val b = joinedGuilds > 1

        val builder = EmbedBuilder()
        builder.setTitle("About me")
        builder.setDescription("Hey, I am $botName!\n\n" +
                               "My current ping to this server is: ${ping}ms\n" +
                               "There ${if (b) "are" else "is"} $joinedGuilds ${if (b) "Discords" else "Discord"} I am currently on.\n" +
                               "Together you already typed $totalCommandsUsed Commands to me.\n\n" +
                               "I was created by $author. (You can find him on ${ChatUtils.textToUrlText(codingGroup, supportDiscord)})\n" +
                               "If you think I am good enough for your Discord invite me with this ${ChatUtils.textToUrlText("link", inviteLink)}\n" +
                               "Cya all! :D")

        ChatUtils.sendEmbed(builder, message.textChannel, 0L, null)
    }

    override fun response(channel: TextChannel) { }

    private fun getPing(channel: MessageChannel): Long {

        return channel.jda.restPing.complete()
    }
    private fun commandCounter(): Int {
        val guilds = DGuild.getAll()
        var commandsUsed = 0

        for (guild in guilds) {
            commandsUsed += guild.commandsUsed
        }

        return commandsUsed
    }
}