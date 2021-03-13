package eu.dragoncoding.dragonbot.utils.tasks

import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.managers.GuildManager
import eu.dragoncoding.dragonbot.structures.AudioState
import java.util.*

class DashboardUpdater : TimerTask() {

    override fun run() {

        for (guild in Bot.shardMan.guilds) {
            val dGuild = GuildManager.getGuild(guild.idLong)

            if (dGuild.musicManager.hasDashboard()) {
                if (dGuild.musicManager.state == AudioState.PLAYING) {

                    dGuild.musicManager.dashboard!!.updateNowPlaying()

                }
            }
        }

    }
}