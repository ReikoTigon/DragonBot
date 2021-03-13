@file:JvmName("Const")
package eu.dragoncoding.dragonbot

import eu.dragoncoding.dragonbot.utils.JSONLoader
import net.dv8tion.jda.api.requests.GatewayIntent
import java.awt.Color
import java.util.*

const val logPathWin = ".\\Logs\\"
const val logPathLin = "/var/www/html/Logs/DragonBot/"
const val statPathWin = ".\\Logs\\stats.html"
const val statPathLin = "/var/www/html/Logs/DragonBot/stats.html"

const val jsonPath = "DONOTOPEN.json"


//ToDo check which are needed - Did but not 100% safe
var Intents: EnumSet<GatewayIntent> = EnumSet.of(GatewayIntent.GUILD_MEMBERS,
                                                 GatewayIntent.GUILD_EMOJIS,
                                                 GatewayIntent.GUILD_VOICE_STATES,
                                                 GatewayIntent.GUILD_MESSAGES,
                                                 GatewayIntent.GUILD_MESSAGE_REACTIONS,
                                                 GatewayIntent.DIRECT_MESSAGES)


//Console
const val checkCmd = ""
const val exitCmd = "exit"
const val restartCmd = "restart"

const val checkResponse = "Still Working"
const val unknownResponse = "Unknown Command:"

const val urlAttributes = "?autoReconnect=true" +  "&useUnicode=true" + "&characterEncoding=UTF-8" +
                          "&useJDBCCompliantTimezoneShift=true" + "&useLegacyDatetimeCode=false" +
                          "&serverTimezone=UTC" + "&autoReconnect=true" + "&useSSL=FALSE"

const val defaultPrefix = "-"
const val defaultChannelID = -1L

const val codingGroup = "DragonCoding"
const val providedBy = "provided by $codingGroup"

const val undefinedError = "Undefined Message"

val colorDefault = Color(0x09a3eb)
val colorInfo = Color(0x5AFFBD)
val colorError = Color(0xFF0000)

const val titleInfo = "INFO"
const val titleError = "ERROR"

var botChannelTopic = "The default Channel to manage the bot. " +
                      "\n(Can be renamed. Please don't delete it.) " +
                      "\nIssues with the bot? Message me: ${JSONLoader.contactDiscord}"

var botHelloMessage = "I'm DragonBot. A bot capable of various tasks. \n" +
                      " \n" +
                      "See '#d startup' to get some Instructions on how to work with me. \n" +
                      "See '#d help' to get some help :D \n" +
                      " \n" +
                      "Issues with the bot? Message me: ${JSONLoader.contactDiscord}"

const val botChannelInfo = "This is the channel I will send information to. \n" +
                           "This also the only channel you can configure me trough, \n" +
                           "so make sure this channel always exists. \n" +
                           "Please restrict access to this channel by roles, so nobody without permission can configure me ;) \n" +
                           " \n" +
                           "If you accidentally delete this channel you can always use '#d restore' to recreate all structures needed by the bot."

const val maxSongsInQueue = 300
const val maxSongsInHistory = 50

val dashboardReactions = arrayOf("🔉", "🔊", "🔄", "🔀", "⏹", "⏮", "⏸", "⏭", "🔁", "🔂", "❓")