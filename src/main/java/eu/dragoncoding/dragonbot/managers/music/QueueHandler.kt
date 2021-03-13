package eu.dragoncoding.dragonbot.managers.music

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.maxSongsInHistory
import eu.dragoncoding.dragonbot.maxSongsInQueue
import eu.dragoncoding.dragonbot.structures.AudioState
import eu.dragoncoding.dragonbot.structures.SongQueue

class QueueHandler(dGuild: DGuild) {

    private val musicController: GuildMusicController = dGuild.musicManager

    private val queue: SongQueue = SongQueue(maxSongsInQueue)
    private val history: SongQueue = SongQueue(maxSongsInHistory)

    fun addSong(track: AudioTrack): Int {
        var state = 0

        if (!musicController.audioPlayer.startTrack(track, true)) {
            state = if (queue.offer(track)) 1 else 2 //1 = Added to queue, 2 = Queue full - not added
        } else {
            musicController.state = AudioState.PLAYING
            history.forceOffer(track.makeClone())
        }

        return state
    }

    fun playNext(force: Boolean): Boolean {
        val nextTrack: AudioTrack? = queue.getNextAndRemove()

        if (nextTrack != null) {
            val started = musicController.audioPlayer.startTrack(nextTrack, !force)

            if (started) {
                history.forceOffer(nextTrack.makeClone())
            }

            return started
        } else {
            musicController.audioPlayer.stopTrack()
            return false
        }
    }

    fun playLast(): Boolean {
        val currTrack: AudioTrack = musicController.audioPlayer.playingTrack
        val lastTrack: AudioTrack? = history.getNext()

        if (lastTrack != null) {
            val started = musicController.audioPlayer.startTrack(lastTrack, false)

            if (started){
                queue.offer(currTrack.makeClone(), 0)
                history.remove(lastTrack)
            }

            return started
        } else {
            return false
        }
    }

    fun getQueue(): ArrayList<AudioTrack> {
        return queue.getQueueList()
    }

    fun shuffle() {
        queue.shuffle()
    }

    fun clear() {
        queue.clear()
    }

}