package eu.dragoncoding.dragonbot.structures

import com.sedmelluq.discord.lavaplayer.track.AudioTrack

class SongQueue(songLimit: Int) {

    private val list: ArrayList<AudioTrack> = ArrayList()
    private val maxSongs: Int = songLimit

    fun offer(track: AudioTrack, index: Int = -1): Boolean {
        if (list.size < maxSongs) {
            if (index != -1) {
                list.add(index, track)
            } else {
                list.add(0, track)
            }

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
        return if (list.isNotEmpty()) {
            list.removeFirst()
        } else {
            null
        }
    }
    fun getNext(): AudioTrack? {
        return if (list.isNotEmpty()) {
            list[0]
        } else {
            null
        }
    }

    fun getLastAndRemove(): AudioTrack? {
        return if (list.isNotEmpty()) {
            list.removeLast()
        } else {
            null
        }
    }
    fun getLast(): AudioTrack? {
        return if (list.isNotEmpty()) {
            list.last()
        } else {
            null
        }
    }

    fun remove(track: AudioTrack) {
        list.remove(track)
    }

    fun hasNext(): Boolean {
        return list.isNotEmpty()
    }

    fun getQueueList(): ArrayList<AudioTrack> {
        val listTemp: ArrayList<AudioTrack> = ArrayList()
        listTemp.addAll(list)

        return listTemp
    }

    fun shuffle() {
        list.shuffle()
    }

    fun clear() {
        list.clear()
    }
}