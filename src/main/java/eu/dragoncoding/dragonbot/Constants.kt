@file:JvmName("Const")
package eu.dragoncoding.dragonbot

import net.dv8tion.jda.api.requests.GatewayIntent
import java.util.*

const val logPathWin = ".\\Logs\\"
const val logPathLin = "/var/www/html/Logs/DragonBot/"
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

const val checkResponse = "Still Working"
const val unknownResponse = "Unknown Command:"