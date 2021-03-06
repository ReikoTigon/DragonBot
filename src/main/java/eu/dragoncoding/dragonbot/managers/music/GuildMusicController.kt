package eu.dragoncoding.dragonbot.managers.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.structures.AudioState
import eu.dragoncoding.dragonbot.structures.PlayType
import net.dv8tion.jda.api.entities.Guild

class GuildMusicController(guild_ID: Long) {

    val audioPlayer: AudioPlayer = Bot.audioPlayerManager.createPlayer()
    lateinit var queueHandler: QueueHandler
    var state: AudioState = AudioState.IDLE
    var playType: PlayType = PlayType.NORMAL

    init {
        val guild: Guild = Bot.shardMan.getGuildById(guild_ID)!!

        guild.audioManager.sendingHandler = AudioPlayerSendHandler(audioPlayer)

        audioPlayer.volume = 10
    }

    fun postInit() {
        audioPlayer.addListener(AudioPlayerEventHandler(audioPlayer))
        queueHandler = QueueHandler(audioPlayer)
    }

}