package eu.dragoncoding.dragonbot.utils

import eu.dragoncoding.dragonbot.*
import net.dv8tion.jda.api.entities.TextChannel
import java.awt.Color
import net.dv8tion.jda.api.EmbedBuilder
import java.util.*
import java.util.concurrent.TimeUnit

object ChatUtils {

    @JvmStatic
    /**
     * Sends a EmbedMessage to the designated channel.
     * Uses provided Strings to build it.
     *
     * @param title       Sets the Title of the EmbedMessage.
     *
     *
     * @param description Sets the Description of the EmbedMessage.
     *
     *
     * @param channel     The Channel the EmbedMessage should be sent to.
     *
     *
     * @param deleteAfter (Optional) Time (in Seconds, Long) after which the sent message should be deleted. If '0' it won't be deleted.
     *
     *
     * @param color       (Optional) The EmbedMessages Blocks Color. Use 'new Color(Hexcode)' or 'new Color(RGB Code)'
     *
     *
     * @author DragonCoding
     * @since 15-05-2020
     */
    fun sendEmbed(title: String, description: String, channel: TextChannel, deleteAfter: Long, color: Color?) {
        val builder = EmbedBuilder()
        builder.setTitle(title)
        builder.setDescription(description)
        sendEmbed(builder, channel, deleteAfter, color)
    }

    @JvmStatic
    /**
     * Sends a embedded Message to the designated channel.
     * Uses a provided [EmbedBuilder] to build it.
     *
     * @param builder     An finished but not built [EmbedBuilder].
     *
     *
     * @param channel     The Channel the embedded Message should be sent to.
     *
     *
     * @param deleteAfter (Optional) Time (in Seconds, Long) after which the sent message should be deleted.
     *
     *If '0' it wont be deleted.
     *
     *
     * @param color       (Optional) The embedded Message Blocks [Color].
     *
     *Use 'new Color(Hexcode)' or 'new Color(RGB Code)'
     *
     *
     *
     *
     * @author DragonCoding
     * @since 15-05-2020
     */
    fun sendEmbed(builder: EmbedBuilder, channel: TextChannel, deleteAfter: Long, color: Color?) {
        builder.setColor(Optional.ofNullable(color).orElse(colorDefault))
        if (deleteAfter == 0L) {
            channel.sendMessage(builder.build()).queue()
        } else {
            channel.sendMessage(builder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS)
        }
    }

    @JvmStatic
    /**
     * Sends a embedded Message to the designated channel.
     * Used to give a command user a response.
     *
     * @param responseMessage     The message to be sent.
     *
     *
     * @param channel     The Channel the embedded Message should be sent to.
     *
     *
     * @param wasSuccessful If the command was executed successfull. Declare wether the message is an ERROR or an INFO.
     *
     *
     * @author DragonCoding
     * @since 07-11-2020
     */
    fun sendResponseToDiscord(responseMessage: String, channel: TextChannel, wasSuccessful: Boolean) {
        val title: String = if (wasSuccessful) titleInfo else titleError
        val color = if (wasSuccessful) colorInfo else colorError
        val deleteAfter = if (wasSuccessful) 10L else 25L
        sendEmbed(title, responseMessage, channel, deleteAfter, color)
    }

    @JvmStatic
    /**
     * Sends a embedded Message to the designated channel.
     * Used to give a command user a response.
     *
     * @param errorCode The state the command exits with. 0 = successful, everythin else means it was an error.
     *
     *
     * @param channel The Channel the embedded Message should be sent to.
     *
     *
     * @param messages An [ArrayList] from the type String with all the response messages.
     * Depending on the errorCode the function will choose an index to be the message.
     *
     *
     *
     * @author DragonCoding
     * @since 07-11-2020
     */
    fun sendResponseToDiscord(errorCode: Int, channel: TextChannel, messages: ArrayList<String>) {
        val message = if (messages.size >= errorCode + 1) messages[errorCode] else undefinedError
        sendResponseToDiscord(message, channel, errorCode == 0)
    }
}