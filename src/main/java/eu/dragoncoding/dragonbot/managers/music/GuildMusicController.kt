package eu.dragoncoding.dragonbot.managers.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.managers.GuildManager
import eu.dragoncoding.dragonbot.structures.AudioState
import eu.dragoncoding.dragonbot.structures.PlayType
import eu.dragoncoding.dragonbot.utils.SettingUtils
import net.dv8tion.jda.api.entities.Guild

class GuildMusicController(guild_ID: Long) {

    val audioPlayer: AudioPlayer = Bot.audioPlayerManager.createPlayer()
    var state: AudioState = AudioState.IDLE
    var playType: PlayType = PlayType.NORMAL

    lateinit var dGuild: DGuild
        private set
    lateinit var queueHandler: QueueHandler
        private set
    var dashboard: MusicDashboard? = null


    init {
        val guild: Guild = Bot.shardMan.getGuildById(guild_ID)!!

        guild.audioManager.sendingHandler = AudioPlayerSendHandler(audioPlayer)

        audioPlayer.volume = 10
    }

    fun postInit() {
        dGuild = GuildManager.getGuild(GuildManager.getGuildByAudioPlayerHash(audioPlayer.hashCode()))

        audioPlayer.addListener(AudioPlayerEventHandler(dGuild))
        queueHandler = QueueHandler(dGuild)

        if (SettingUtils.checkMusicDashboard(dGuild)) {
            dashboard = MusicDashboard(dGuild)
        }
    }

    fun hasDashboard(): Boolean {
        return dashboard != null
    }
}