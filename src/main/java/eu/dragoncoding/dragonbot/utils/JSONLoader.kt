package eu.dragoncoding.dragonbot.utils

import eu.dragoncoding.dragonbot.Main.shutDown
import eu.dragoncoding.dragonbot.jsonPath
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.ParseException
import org.slf4j.LoggerFactory
import java.io.IOException

object JSONLoader {
    private val logger = LoggerFactory.getLogger(JSONLoader::class.java)
    val token: String
        get() = getJsonAttribute("token")
    val dbUser: String
        get() = getJsonAttribute("dbUser")
    val dbPassword: String
        get() = getJsonAttribute("dbPassword")
    val dbUrl: String
        get() = getJsonAttribute("dbUrl")
    val dbName: String
        get() = getJsonAttribute("dbName")

    private fun getJsonAttribute(attribute: String): String {
        val jsonParser = JSONParser()
        val json = File(jsonPath)
        if (json.exists()) {
            try {
                FileReader(json).use { reader ->
                    val jObject: Any = jsonParser.parse(reader)
                    val jsonArray = jObject as JSONArray
                    val jsonObject = jsonArray[0] as JSONObject

                    return jsonObject[attribute].toString()
                }
            } catch (e: ParseException) {
                logger.error("Error whilst parsing SecuredData", e)
                shutDown = true
                return ""
            } catch (e: IOException) {
                logger.error("Error whilst parsing SecuredData", e)
                shutDown = true
                return ""
            }
        } else {
            logger.error("No SecuredData found at '" + json.absolutePath + "'")
            shutDown = true
            return ""
        }
    }
}