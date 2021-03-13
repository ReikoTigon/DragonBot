package eu.dragoncoding.dragonbot.hibernate.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "GSettings")
class GSettings: eu.dragoncoding.dragonbot.structures.Entity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    val id: Long = 0

    @Column(name = "nowPlaying", nullable = false)
    var showNowPlaying: Boolean = true

    @Column(name = "deleteCmd", nullable = false)
    var deleteCommands: Boolean = true

    @Column(name = "musicDashboard", nullable = false)
    var musicDashboard: Boolean = false
}