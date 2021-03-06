package eu.dragoncoding.dragonbot.hibernate.entities

import eu.dragoncoding.dragonbot.Bot
import eu.dragoncoding.dragonbot.defaultChannelID
import eu.dragoncoding.dragonbot.defaultPrefix
import eu.dragoncoding.dragonbot.hibernate.EntityDao
import eu.dragoncoding.dragonbot.managers.GuildManager.removeGuild
import eu.dragoncoding.dragonbot.managers.music.GuildMusicController
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "Guilds")
class DGuild: eu.dragoncoding.dragonbot.structures.Entity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    val id: Long = 0

    @Column(name = "guildID", nullable = false)
    var guildID: Long = defaultChannelID
    @Column(name = "prefix", nullable = false)
    var prefix: String = defaultPrefix
    @Column(name = "isJoined", nullable = false)
    var isJoined: Boolean = true
    @Column(name = "commandsUsed", nullable = false)
    var commandsUsed: Int = 0

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "settings", nullable = false)
    var settings: GSettings = GSettings()

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "channels", nullable = false)
    var channels: GChannels = GChannels()


    @Transient
    lateinit var musicManager: GuildMusicController

    constructor(guild_ID: Long) {
        this.guildID = guild_ID
    }
    constructor()

    fun save() {
        EntityDao.save(this)
    }
    fun update() {
        EntityDao.update(this)
    }

    fun activate() {
        if (!isJoined) {
            isJoined = true
            update()
        }

        if (channels.botChannelID != defaultChannelID) {
            val channel = Bot.shardMan.getGuildById(guildID)?.getTextChannelById(channels.botChannelID)
            if (channel == null) {
                channels.botChannelID = defaultChannelID
            }
        }

        if (channels.musicChannelID != defaultChannelID) {
            val channel = Bot.shardMan.getGuildById(guildID)?.getTextChannelById(channels.musicChannelID)
            if (channel == null) {
                channels.musicChannelID = defaultChannelID
            }
        }

        update()
    }
    fun deactivate() {
        if (isJoined) {
            isJoined = false
            update()
            removeGuild(guildID)
        }
    }

    fun incrementCommands() {
        this.commandsUsed += 1
        update()
    }

    companion object {
        fun getByGuildID(guild_ID: Long): DGuild? {
            val criteriaPackage = EntityDao.createCriteria(DGuild::class.java)
            val cb = criteriaPackage.criteriaBuilder
            val query = criteriaPackage.query
            val root = criteriaPackage.root
            query.select(root).where(cb.equal(root.get<Any>("guildID"), guild_ID))

            return EntityDao.get(DGuild::class.java, query)
        }

        fun getAll(): ArrayList<DGuild> {
            return EntityDao.getAll(DGuild::class.java)
        }
        fun getAllActive(): ArrayList<DGuild> {
            val criteriaPackage = EntityDao.createCriteria(DGuild::class.java)
            val cb = criteriaPackage.criteriaBuilder
            val query = criteriaPackage.query
            val root = criteriaPackage.root
            query.select(root).where(cb.equal(root.get<Any>("isJoined"), true))

            return EntityDao.getAllByQuery(DGuild::class.java, query)
        }
    }
}