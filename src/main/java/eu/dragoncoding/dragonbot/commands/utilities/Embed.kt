package eu.dragoncoding.dragonbot.commands.utilities

import eu.dragoncoding.dragonbot.colorDefault
import eu.dragoncoding.dragonbot.structures.Command
import eu.dragoncoding.dragonbot.utils.ChatUtils
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class Embed: Command {

    override val cmdLength: Int = "embed".length + 1
    override var errorCode: Int = 0

    override fun performCommand(message: Message, subString: Int) {
        removeMessageIfActivated(message)

        var msg = message.contentRaw.substring(subString + cmdLength)
        msg = msg.replace("\\n", "\n", true)
        ChatUtils.sendEmbed(null, msg, message.textChannel, 0L, colorDefault)
    }

    override fun response(channel: TextChannel) { }
}