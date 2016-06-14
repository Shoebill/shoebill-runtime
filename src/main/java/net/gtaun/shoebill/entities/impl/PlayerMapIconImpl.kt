package net.gtaun.shoebill.entities.impl

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.constant.MapIconStyle
import net.gtaun.shoebill.data.Color
import net.gtaun.shoebill.data.Vector3D
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.PlayerMapIcon
import net.gtaun.shoebill.exception.CreationFailedException
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author MK124
 * @author Marvin Haschker
 */
class PlayerMapIconImpl internal constructor(override val player: Player) : PlayerMapIcon {

    private val emptyIds: Queue<Int> = ConcurrentLinkedQueue<Int>()
    private val mapIcons: Array<PlayerMapIcon.MapIcon?> = arrayOfNulls(101)

    init {
        (1..100).forEach { emptyIds.add(it) }
    }

    private fun pullRandomId(): Int {
        val id = emptyIds.firstOrNull() ?: throw CreationFailedException("There are no empty ids left.")
        emptyIds.remove(id)
        return id
    }

    override operator fun get(value: Int): PlayerMapIcon.MapIcon? = mapIcons[value]

    override fun get(): Collection<PlayerMapIcon.MapIcon> = mapIcons.requireNoNulls().asList()

    override fun setIcon(iconid: Int, icon: PlayerMapIcon.MapIcon) {
        mapIcons[iconid] = icon
    }

    override fun createIcon(x: Float, y: Float, z: Float, markerType: Int, color: Color, style: MapIconStyle):
            PlayerMapIcon.MapIcon = createIcon(x, y, z, markerType, color, style, pullRandomId())

    override fun createIcon(pos: Vector3D, markerType: Int, color: Color, style: MapIconStyle): PlayerMapIcon.MapIcon =
            createIcon(pos, markerType, color, style, pullRandomId())

    override fun createIcon(x: Float, y: Float, z: Float, markerType: Int, color: Color, style: MapIconStyle,
                            iconId: Int): PlayerMapIcon.MapIcon {
        val icon = createIcon(iconId)
        icon.update(x, y, z, markerType, color, style)
        setIcon(iconId, icon)
        return icon
    }

    override fun createIcon(pos: Vector3D, markerType: Int, color: Color, style: MapIconStyle,
                            iconId: Int): PlayerMapIcon.MapIcon {
        val icon = createIcon(iconId)
        icon.update(pos, markerType, color, style)
        setIcon(iconId, icon)
        return icon
    }

    override fun createIcon(): PlayerMapIcon.MapIcon {
        val id = pullRandomId()
        val mapIcon = MapIconImpl(id, player)
        setIcon(id, mapIcon)
        return mapIcon
    }

    override fun createIcon(iconId: Int): PlayerMapIcon.MapIcon {
        if (emptyIds.contains(iconId))
            emptyIds.remove(iconId)

        val mapIcon = MapIconImpl(iconId, player)
        setIcon(iconId, mapIcon)
        return mapIcon
    }

    inner class MapIconImpl internal constructor(id: Int, override val player: Player) : PlayerMapIcon.MapIcon {

        override var id = id
            private set

        override val isDestroyed: Boolean
            get() = id == -1

        override fun destroy() {
            if (isDestroyed) return

            SampNativeFunction.removePlayerMapIcon(player.id, id)
            emptyIds.add(id)

            id = -1
        }

        override fun update(pos: Vector3D, markerType: Int, color: Color, style: MapIconStyle) =
                update(pos.x, pos.y, pos.z, markerType, color, style)

        override fun update(x: Float, y: Float, z: Float, markerType: Int, color: Color, style: MapIconStyle) =
                SampNativeFunction.setPlayerMapIcon(player.id, id, x, y, z, markerType, color.value, style.value)
    }
}
