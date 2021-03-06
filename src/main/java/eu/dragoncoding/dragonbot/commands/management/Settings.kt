package eu.dragoncoding.dragonbot.commands.management

import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.managers.GuildManager
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.utils.ChannelUtils
import eu.dragoncoding.dragonbot.utils.ChatUtils
import eu.dragoncoding.dragonbot.utils.SettingUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import java.time.format.DateTimeFormatter

class Settings: Command {
    override val cmdLength: Int = "settings".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int) {
        removeMessageIfActivated(message)


        val dGuild: DGuild = GuildManager.getGuild(message.guild.idLong)
        val channel: TextChannel = message.textChannel

        ChatUtils.sendEmbed(getSettingsAsBuilder(dGuild), channel, 0L, null)
    }

    private fun getSettingsAsBuilder(dGuild: DGuild): EmbedBuilder {
        val dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

        val avatarUrl: String? = Bot.shardMan.getGuildById(dGuild.guildID)!!.iconUrl
        val guildName: String = Bot.shardMan.getGuildById(dGuild.guildID)!!.name

        val joinedAt = Bot.shardMan.getGuildById(dGuild.guildID)!!.selfMember.timeJoined.format(dtf)
        val commandsUsed: Int = dGuild.commandsUsed
        val guildPrefix: String = dGuild.prefix

        val botChannel: String = ChannelUtils.getTextChannelFromDGuild(dGuild, dGuild.channels.botChannelID)?.asMention ?: "N/A"
        var npChannel: String = "Disabled"

        val delCmd: Boolean = SettingUtils.checkDeleteCmd(dGuild)

        if (SettingUtils.checkNowPlaying(dGuild)) {
            var temp = ChannelUtils.getTextChannelFromDGuild(dGuild, dGuild.channels.musicChannelID)
            if (temp != null) {
                npChannel = temp.asMention
            } else {
                temp = ChannelUtils.getTextChannelFromDGuild(dGuild, dGuild.channels.tempChannelID_1)
                npChannel = temp?.asMention ?: "N/A"
            }
        }

        val builder = EmbedBuilder()
        builder.setThumbnail(avatarUrl)

        builder.setTitle("Bot-Settings of $guildName")
        builder.setDescription("Bot join date: $joinedAt\n" +
                               "Commands used since then: $commandsUsed\n" +
                               "The Prefix is '$guildPrefix'")

        builder.addBlankField(false)
        builder.addField("**Music**", " ", false)
        builder.addField("Music Channel", npChannel, false)
        builder.addBlankField(false)
        builder.addField("**Management**", " ", false)
        builder.addField("Bot Config Channel", botChannel, false)
        builder.addField("Delete Commands", delCmd.toString(), false)

        return builder
    }

    override fun response(channel: TextChannel) {
        TODO("No response needed!")
    }
}