package eu.dragoncoding.dragonbot.hibernate.entities

import eu.dragoncoding.dragonbot.defaultChannelID
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "GChannels")
class GChannels: eu.dragoncoding.dragonbot.structures.Entity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    val id: Long = 0

    @Column(name = "botChannel", nullable = false)
    var botChannelID: Long = defaultChannelID

    @Column(name = "nowPlaying", nullable = false)
    var musicChannelID: Long = defaultChannelID


    @Transient var tempChannelID_1: Long = defaultChannelID //Music
//    @Transient var tempChannelID_2: Long = defaultChannelID //XY
//    @Transient var tempChannelID_3: Long = defaultChannelID //XY
//    @Transient var tempChannelID_4: Long = defaultChannelID //XY
}