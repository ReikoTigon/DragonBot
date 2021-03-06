package eu.dragoncoding.dragonbot.managers.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import eu.dragoncoding.dragonbot.managers.GuildManager
import eu.dragoncoding.dragonbot.maxSongsInHistory
import eu.dragoncoding.dragonbot.maxSongsInQueue
import eu.dragoncoding.dragonbot.structures.SongQueue

class QueueHandler(player: AudioPlayer) {

    private val musicController: GuildMusicController

    private val queue: SongQueue = SongQueue(maxSongsInQueue)
    private val history: SongQueue = SongQueue(maxSongsInHistory)

    fun addSong(track: AudioTrack): Int {
        var state = 0

        if (!musicController.audioPlayer.startTrack(track, true)) {
            state = if (queue.offer(track)) 1 else 2 //1 = Added to queue, 2 = Queue full - not added
        } else {
            history.forceOffer(track.makeClone())
        }

        return state
    }

    fun playNext(force: Boolean): Boolean {

        val nextTrack: AudioTrack? = queue.getNextAndRemove()
        musicController.audioPlayer.startTrack(nextTrack, !force)

        return queue.hasNext()
    }

    fun getQueue(): ArrayList<AudioTrack> {
        return queue.getQueueList()
    }

    fun clear() {
        queue.clear()
    }

    init {
        val guildId = GuildManager.getGuildByAudioPlayerHash(player.hashCode())
        musicController = GuildManager.getGuild(guildId).musicManager
    }
}