package eu.dragoncoding.dragonbot

import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import eu.dragoncoding.dragonbot.utils.JSONLoader
import eu.dragoncoding.dragonbot.utils.tasks.BotMessage
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.dv8tion.jda.api.OnlineStatus
import javax.security.auth.login.LoginException
import net.dv8tion.jda.api.JDA
import java.lang.InterruptedException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.function.Consumer
import kotlin.system.exitProcess

object Bot {
    private val logger: Logger = LoggerFactory.getLogger(Bot::class.java)
    val timer: Timer = Timer(false)
    lateinit var shardMan: ShardManager
        private set

    fun startUp() {
        shardMan = createShardManager()
        awaitJdaReady()

        runConsoleListener()

        timer.scheduleAtFixedRate(BotMessage(), 0L, 10 * 60 * 1000)
    }
    fun shutDown() {
        shardMan.setStatus(OnlineStatus.OFFLINE)
        shardMan.shutdown()

        timer.cancel()

        logger.info("Bot successfully shut down!")
    }


    private fun createShardManager(): ShardManager {
        val shardMan: ShardManager
        try {
            val builder = DefaultShardManagerBuilder.create(JSONLoader.token, Intents)
            builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
            builder.setStatus(OnlineStatus.ONLINE)
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
        logger.info("Bot started successfully!")
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
            exitCmd -> {
                Main.shutDown = true
            }
            //updateCmd -> Game.loadGamesFromDB()
            else -> logger.info(unknownResponse)
        }
    }
}