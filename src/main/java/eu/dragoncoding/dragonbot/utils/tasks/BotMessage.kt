package eu.dragoncoding.dragonbot.utils.tasks

import eu.dragoncoding.dragonbot.Bot
import net.dv8tion.jda.api.entities.Activity
import java.util.*
import kotlin.random.Random

class BotMessage : TimerTask() {
    override fun run() {
        when (Random.nextInt(0, 1)) {
            0 -> Bot.shardMan.setActivity(Activity.listening(getListeningString()))
            1 -> Bot.shardMan.setActivity(Activity.playing(getPlayingString()))
        }
    }


    private fun getListeningString(): String {
        val listenStrings = arrayOf("your commands",
                                    "DragonCoding",
                                    "you. Yes you!")

        val value = Random.nextInt(listenStrings.size)
        return listenStrings[value]
    }
    private fun getPlayingString(): String {
        val playStrings = arrayOf("music for ya",
                                  "'How to fail at coding'",
                                  "dead X.X")

        val value = Random.nextInt(playStrings.size)
        return playStrings[value]
    }
}