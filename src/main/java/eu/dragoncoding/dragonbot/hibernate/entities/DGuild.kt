package eu.dragoncoding.dragonbot.hibernate.entities

import eu.dragoncoding.dragonbot.defaultChannelID
import eu.dragoncoding.dragonbot.defaultPrefix
import eu.dragoncoding.dragonbot.hibernate.EntityDao
import eu.dragoncoding.dragonbot.managers.GuildManager.removeGuild
import lombok.Data
import lombok.NoArgsConstructor
import org.slf4j.LoggerFactory
import javax.persistence.*

@Entity
@Data
@NoArgsConstructor
@Table(name = "Guilds")

class DGuild: eu.dragoncoding.dragonbot.structures.Entity {

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
    @Column(name = "botChannelID", nullable = false)
    var botChannelID: Long = defaultChannelID
    @Column(name = "commandsUsed", nullable = false)
    var commandsUsed: Int = 0

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
    }

    fun deactivate() {
        if (isJoined) {
            isJoined = false
            update()
            removeGuild(guildID)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DGuild::class.java)

        fun getByGuildID(guild_ID: Long): DGuild? {
            val criteriaPackage = EntityDao.createCriteria(DGuild::class.java)
            val cb = criteriaPackage.criteriaBuilder
            val query = criteriaPackage.query
            val root = criteriaPackage.root
            query.select(root).where(cb.equal(root.get<Any>("guild_ID"), guild_ID))

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