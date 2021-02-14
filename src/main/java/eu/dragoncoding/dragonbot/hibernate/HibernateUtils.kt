package eu.dragoncoding.dragonbot.hibernate

import eu.dragoncoding.dragonbot.hibernate.entities.DGuild
import eu.dragoncoding.dragonbot.urlAttributes
import eu.dragoncoding.dragonbot.utils.JSONLoader.dbName
import eu.dragoncoding.dragonbot.utils.JSONLoader.dbUrl
import eu.dragoncoding.dragonbot.utils.JSONLoader.dbUser
import eu.dragoncoding.dragonbot.utils.JSONLoader.dbPassword
import org.hibernate.SessionFactory
import org.hibernate.cfg.Environment
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.service.ServiceRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.*
import kotlin.system.exitProcess

object HibernateUtils {

    private val logger: Logger = LoggerFactory.getLogger(HibernateUtils::class.java)
    private var sessionFactory: SessionFactory


    init {
        sessionFactory = getSessFactory()
    }

    fun getFactory(): SessionFactory {
        return if (!sessionFactory.isOpen) {
            getSessFactory()
        } else {
            sessionFactory
        }
    }

    private fun getSessFactory(): SessionFactory {
        try {
            val mappedClasses = ArrayList<Class<*>>()

            //Add classes to map here!
            mappedClasses.add(DGuild::class.java)

            sessionFactory = createSessionFactory(dbName, mappedClasses)
            return sessionFactory
        } catch (e: Exception) {
            logger.error("Error whilst connecting to Database: ", e)
            exitProcess(-1)
        }
    }

    private fun createSessionFactory(dbName: String, mappedClasses: ArrayList<Class<*>>): SessionFactory {
        val configuration = Configuration()

        // Hibernate settings equivalent to hibernate.cfg.xml
        val settings = Properties()
        settings[Environment.DRIVER] = "com.mysql.cj.jdbc.Driver"
        settings[Environment.URL] = dbUrl + dbName + urlAttributes
        settings[Environment.USER] = dbUser
        settings[Environment.PASS] = dbPassword
        settings[Environment.DIALECT] = "org.hibernate.dialect.MySQL8Dialect"
        settings[Environment.ENABLE_LAZY_LOAD_NO_TRANS] = true
        settings[Environment.SHOW_SQL] = "false"
        settings[Environment.CURRENT_SESSION_CONTEXT_CLASS] = "thread"
        settings[Environment.HBM2DDL_AUTO] = "update"
        configuration.properties = settings

        //Mapping
        for (c in mappedClasses) {
            configuration.addAnnotatedClass(c)
        }
        val serviceRegistry: ServiceRegistry = StandardServiceRegistryBuilder()
            .applySettings(configuration.properties).build()
        return configuration.buildSessionFactory(serviceRegistry)
    }

    fun shutdown() {
        sessionFactory.close()
    }
}