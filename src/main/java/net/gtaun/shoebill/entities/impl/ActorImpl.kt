package net.gtaun.shoebill.entities.impl

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.SampObjectStoreImpl
import net.gtaun.shoebill.data.AngledLocation
import net.gtaun.shoebill.data.Vector3D
import net.gtaun.shoebill.entities.Actor
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.EventManagerNode

/**
 * @author Marvin Haschker
 */
class ActorImpl constructor(eventManager: EventManager, store: SampObjectStoreImpl,
                                          override val model: Int, pos: Vector3D, angle: Float) : Actor() {

    override var id = SampNativeFunction.createActor(model, pos.x, pos.y, pos.z, angle)
        private set

    private val eventManagerNode: EventManagerNode = eventManager.createChildNode()

    init {
        if(id == INVALID_ACTOR) throw CreationFailedException()
        store.setActor(id, this)
    }

    override var location: AngledLocation
        get() {
            val pos = AngledLocation()
            SampNativeFunction.getActorPos(id, pos)

            pos.angle = SampNativeFunction.getActorFacingAngle(id)
            pos.worldId = SampNativeFunction.getActorVirtualWorld(id)

            return pos
        }
        set(loc) {
            SampNativeFunction.setActorPos(id, loc.x, loc.y, loc.z)
            SampNativeFunction.setActorVirtualWorld(id, loc.worldId)
            SampNativeFunction.setActorFacingAngle(id, loc.angle)
        }

    override var virtualWorld: Int
        get() = SampNativeFunction.getActorVirtualWorld(id)
        set(world) {
            SampNativeFunction.setActorVirtualWorld(id, world)
        }

    override var health: Float
        get() = SampNativeFunction.getActorHealth(id)
        set(health) {
            SampNativeFunction.setActorHealth(id, health)
        }

    override var isInvulnerable: Boolean
        get() = SampNativeFunction.isActorInvulnerable(id) > 0
        set(invulnerable) {
            SampNativeFunction.setActorInvulnerable(id, invulnerable)
        }

    override fun applyAnimation(animLib: String, animName: String, time: Int, animSpeed: Float, loop: Boolean,
                                lockX: Boolean, lockY: Boolean, freeze: Boolean) {
        SampNativeFunction.applyActorAnimation(id, animLib, animName, animSpeed, if (loop) 1 else 0,
                if (lockX) 1 else 0, if (lockY) 1 else 0, if (freeze) 1 else 0, time)
    }

    override fun clearAnimation() {
        SampNativeFunction.clearActorAnimations(id)
    }

    override fun isActorStreamedIn(player: Player): Boolean = SampNativeFunction.isActorStreamedIn(id, player.id) > 0

    override fun destroy() {
        if (isDestroyed) return

        SampNativeFunction.destroyActor(id)

        val destroyEvent = DestroyEvent(this)
        eventManagerNode.dispatchEvent(destroyEvent, this)
        eventManagerNode.destroy()

        this.id = Actor.INVALID_ACTOR
    }

    override val isDestroyed: Boolean
        get() = id == Actor.INVALID_ACTOR

}
