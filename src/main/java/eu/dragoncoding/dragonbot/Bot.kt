package eu.dragoncoding.dragonbot

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import eu.dragoncoding.dragonbot.hibernate.HibernateUtils
import eu.dragoncoding.dragonbot.listeners.GuildListener
import eu.dragoncoding.dragonbot.listeners.TextListener
import eu.dragoncoding.dragonbot.managers.GuildManager
import eu.dragoncoding.dragonbot.utils.JSONLoader
import eu.dragoncoding.dragonbot.utils.tasks.BotMessage
import eu.dragoncoding.dragonbot.utils.tasks.StatCollector
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.lang.management.ManagementFactory
import java.util.*
import java.util.function.Consumer
import javax.security.auth.login.LoginException
import kotlin.system.exitProcess

object Bot {
    private val logger: Logger = LoggerFactory.getLogger(Bot::class.java)
    private val timer: Timer = Timer(false)
    lateinit var shardMan: ShardManager
        private set

    val audioPlayerManager: AudioPlayerManager = DefaultAudioPlayerManager()


    /**
     * This function starts the bot. It's the complete startup.
     *
     * @return the time the bot needed for startup (in ms)
     */
    fun startUp(): Long {
        val startTime = System.currentTimeMillis()

        shardMan = createShardManager() //open the connection to discord
        awaitJdaReady()                 //Wait until all Shards are ready

        setupAudioManager()

        HibernateUtils.getFactory() //Connect to DB
        loadDatabaseData() //Load guilds

        runConsoleListener()            //Start the listener for console commands

        timer.scheduleAtFixedRate(BotMessage(), 0L, 10 * 60 * 1000)
        timer.scheduleAtFixedRate(StatCollector(), 0L, 60 * 1000)

        return System.currentTimeMillis() - startTime
    }
    /**
     * This function stops the bot. It's the complete shutdown.
     *
     * @return the time the bot needed for shutdown (in ms)
     */
    fun shutDown(): Long {
        val startTime = System.currentTimeMillis()

        GuildManager.shutdown()

        shardMan.setStatus(OnlineStatus.OFFLINE)
        shardMan.shutdown()

        timer.cancel()

        HibernateUtils.shutdown()

        return System.currentTimeMillis() - startTime
    }

    @Throws(IOException::class)
    fun restart(): String {
        try {
            // java binary
            val java = System.getProperty("java.home") + "/bin/java"
            // vm arguments
            val vmArguments: List<String> = ManagementFactory.getRuntimeMXBean().inputArguments
            val vmArgsOneLine = StringBuilder()

            for (arg in vmArguments) {
                if (!arg.contains("-agentlib")) {
                    vmArgsOneLine.append(arg)
                    vmArgsOneLine.append(" ")
                }
            }
            val cmd = StringBuffer("\"$java\" $vmArgsOneLine")
            val mainCommand: Array<String> = System.getProperty("sun.java.command").split(" ".toRegex()).toTypedArray()

            if (mainCommand[0].endsWith(".jar")) {
                cmd.append("-jar " + File(mainCommand[0]).path)
                for (i in 1 until mainCommand.size) {
                    cmd.append(" ")
                    cmd.append(mainCommand[i])
                }
                Runtime.getRuntime().addShutdownHook(Thread {
                    try {
                        Runtime.getRuntime().exec(cmd.toString())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                })

                return "Restarting the Bot!"
            } else {
                return "Not restarting the Bot! Its in the JDE and not the finished Program!\n   Shutting down normally :)"
            }
        } catch (e: Exception) {
            throw IOException("Error while trying to restart the bot. Shutting down!", e)
        }
    }


    private fun createShardManager(): ShardManager {
        val shardMan: ShardManager
        try {
            val builder = DefaultShardManagerBuilder.create(JSONLoader.token, Intents)
            builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
            builder.setStatus(OnlineStatus.ONLINE)

            //EventListeners
            builder.addEventListeners(GuildListener())
            builder.addEventListeners(TextListener())

            shardMan = builder.build()
        } catch (e: LoginException) {
            logger.error("Error whilst connecting to Discord", e)
            exitProcess(-1)
        }
        return shardMan
    }
    private fun awaitJdaReady() {
        shardMan.shards.forEach(Consumer { jda: JDA ->
            try {
                jda.awaitReady()
            } catch (e: InterruptedException) {
                logger.error("Error whilst startup: ", e)
            }
        })
    }
    private fun loadDatabaseData() {
        for (guild in shardMan.guilds) {
            GuildManager.getGuild(guild.idLong)
        }
    }
    private fun setupAudioManager() {
        val yt = YoutubeAudioSourceManager()
        yt.setPlaylistPageCount(3)

        audioPlayerManager.registerSourceManager(yt)

        AudioSourceManagers.registerRemoteSources(audioPlayerManager)
        audioPlayerManager.configuration.isFilterHotSwapEnabled = true
    }
    private fun runConsoleListener() {
        val consoleListener = Thread {
            try {
                var line: String
                val reader = BufferedReader(InputStreamReader(System.`in`))
                while (reader.readLine().also { line = it } != null) {
                    callAction(line)
                    if (Main.shutDown) {
                        reader.close()
                    }
                }
            } catch (ignored: IOException) {
            }
        }
        consoleListener.name = "ConsoleListener"
        consoleListener.start()
    }
    private fun callAction(line: String) {
        when (line.toLowerCase().split(" ".toRegex()).toTypedArray()[0]) {
            checkCmd -> logger.info(checkResponse)
            exitCmd -> Main.shutDown = true
            restartCmd -> Main.restart = true

            //updateCmd -> Game.loadGamesFromDB()
            else -> logger.info(unknownResponse)
        }
    }
}