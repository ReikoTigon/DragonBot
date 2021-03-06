package eu.dragoncoding.dragonbot.managers

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.managers.music.GuildMusicController
import java.util.concurrent.ConcurrentHashMap

object GuildManager {
    private val guildManager: ConcurrentHashMap<Long, DGuild> = ConcurrentHashMap()

    fun getGuild(guildID: Long): DGuild {
        if (guildManager.containsKey(guildID)) {

            return guildManager[guildID]!!

        } else {
            var guild: DGuild?

            //If in database load it - Can return null!
            guild = loadGuildFromDB(guildID)

            //If not in database create a new guild and save it
            if (guild == null) {
                guild = createNewGuild(guildID)
            }

            //Add it to the GuildManager
            guildManager[guild.guildID] = guild

            guild.musicManager = GuildMusicController(guild.guildID)
            guild.musicManager.postInit()

            return guild
        }
    }

    private fun loadGuildFromDB(guild_ID: Long): DGuild? {
        return DGuild.getByGuildID(guild_ID)
    }
    private fun createNewGuild(guild_ID: Long): DGuild {
        println("New Guild: $guild_ID")
        val guild = DGuild(guild_ID)
        guild.save()

        return guild
    }

    fun getGuildByAudioPlayerHash(hash: Int): Long {
        for (dGuild in guildManager.values) {
            if (dGuild.musicManager.audioPlayer.hashCode() == hash) {
                return dGuild.guildID
            }
        }

        return -1
    }
    fun removeGuild(guild_ID: Long) {
        if (guildManager[guild_ID] != null) {
            guildManager.remove(guild_ID)
        }
    }

    fun shutdown() {
        for (dGuild in guildManager.values) {
            val manager = Bot.shardMan.getGuildById(dGuild.guildID)!!.audioManager
            val player: AudioPlayer = dGuild.musicManager.audioPlayer

            dGuild.musicManager.queueHandler.clear()

            player.stopTrack()

            manager.closeAudioConnection()
        }
    }
}