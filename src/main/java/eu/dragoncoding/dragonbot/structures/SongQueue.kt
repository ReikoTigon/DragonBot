package eu.dragoncoding.dragonbot.structures

import com.sedmelluq.discord.lavaplayer.track.AudioTrack

class SongQueue(songLimit: Int) {

    private val list: ArrayList<AudioTrack> = ArrayList()
    private val maxSongs: Int = songLimit

    fun offer(track: AudioTrack): Boolean {
        if (list.size < maxSongs) {
            list.add(track)
        }

        return list.size < maxSongs
    }
    fun forceOffer(track: AudioTrack) {
        if (list.size >= maxSongs) {
            list.removeFirst()
        }

        list.add(track)
    }

    fun getNextAndRemove(): AudioTrack? {
        if (list.isNotEmpty()) {
            return list.removeFirst()
        } else {
            return null
        }
    }
    fun getNext(): AudioTrack? {
        if (list.isNotEmpty()) {
            return list[0]
        } else {
            return null
        }
    }

    fun getLastAndRemove(): AudioTrack? {
        if (list.isNotEmpty()) {
            return list.removeLast()
        } else {
            return null
        }
    }
    fun getLast(): AudioTrack? {
        if (list.isNotEmpty()) {
            return list.last()
        } else {
            return null
        }
    }

    fun hasNext(): Boolean {
        return list.isNotEmpty()
    }

    fun getQueueList(): ArrayList<AudioTrack> {
        val listTemp: ArrayList<AudioTrack> = ArrayList()
        listTemp.addAll(list)

        return listTemp
    }

    fun clear() {
        list.clear()
    }
}