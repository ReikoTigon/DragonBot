package eu.dragoncoding.dragonbot.managers.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import eu.dragoncoding.dragonbot.managers.GuildManager.getGuild

class AudioPlayerSearchResult(private val guildID: Long) : AudioLoadResultHandler {
    override fun loadFailed(e: FriendlyException) {
        println("Failed: $e")
    }

    override fun noMatches() {
        println("No matches")
    }

    override fun playlistLoaded(playlist: AudioPlaylist) {
        if (playlist.isSearchResult) {
            singleTrack(playlist.tracks[0])
        } else {
             val dGuild = getGuild(guildID)

            for (track in playlist.tracks) {
                dGuild.musicManager.queueHandler.addSong(track)
            }
        }
    }

    override fun trackLoaded(track: AudioTrack) {
        singleTrack(track)
    }

    private fun singleTrack(track: AudioTrack) {
        val dGuild = getGuild(guildID)
        dGuild.musicManager.queueHandler.addSong(track)
    }
}