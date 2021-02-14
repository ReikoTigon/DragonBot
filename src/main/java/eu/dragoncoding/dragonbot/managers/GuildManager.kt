package eu.dragoncoding.dragonbot.managers

import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
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

            return guild
        }
    }

    private fun loadGuildFromDB(guild_ID: Long): DGuild? {
        return DGuild.getByGuildID(guild_ID)
    }

    private fun createNewGuild(guild_ID: Long): DGuild {
        val guild = DGuild(guild_ID)
        guild.save()

        return guild
    }

    fun removeGuild(guild_ID: Long) {
        if (guildManager[guild_ID] != null) {
            guildManager.remove(guild_ID)
        }
    }
}