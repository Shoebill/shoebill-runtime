/**
 * Copyright (C) 2011-2014 MK124
 * Copyright (C) 2011 JoJLlmAn

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill.entities.impl

import net.gtaun.shoebill.CharsetUtils
import net.gtaun.shoebill.DialogEventUtils
import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.SampObjectStore
import net.gtaun.shoebill.constant.*
import net.gtaun.shoebill.data.*
import net.gtaun.shoebill.entities.*
import net.gtaun.shoebill.event.dialog.DialogCloseEvent.DialogCloseType
import net.gtaun.shoebill.exception.AlreadyExistException
import net.gtaun.shoebill.exception.IllegalLengthException
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.EventManagerNode
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.util.*

/**
 * @author MK124
 * @author JoJLlmAn
 * @author Marvin Haschker
 */
class PlayerImpl(eventManager: EventManager, private val store: SampObjectStore, id: Int) : Player() {

    override val keyState: PlayerKeyStateImpl = PlayerKeyStateImpl(this)
        get() {
            field.update()
            return field
        }

    override val attach: PlayerAttach = PlayerAttachImpl(this)
    override val mapIcon: PlayerMapIcon = PlayerMapIconImpl(this)
    override var id = id
        private set

    override var isControllable = true
    override var isStuntBonusEnabled = false
    override var isSpectating = false
    override var isRecording = false

    override val isOnline: Boolean
        get() = id != Player.INVALID_ID

    override var spectatingPlayer: Player? = null
        private set

    override var spectatingVehicle: Vehicle? = null
        private set

    override var updateCount: Long = -1
        private set

    override var weather: Weather = Weather.SUNNY_VEGAS
        set(weather) {
            SampNativeFunction.setPlayerWeather(id, weather.id)
            field = weather
        }

    override var worldBound = Area(-20000.0f, -20000.0f, 20000.0f, 20000.0f)
        get() = field.clone()
        set(value) {
            SampNativeFunction.setPlayerWorldBounds(id, value.maxX, value.minX, value.maxY, value.minY)
            field = value
        }

    override var velocity = Velocity()
        get() {
            SampNativeFunction.getPlayerVelocity(id, field)
            return field
        }
        set(value) {
            SampNativeFunction.setPlayerVelocity(id, value.x, value.y, value.z)
            field.set(value)
        }

    override val weaponSkill: PlayerWeaponSkill = PlayerWeaponSkillImpl(this)

    override var checkpoint: Checkpoint? = null
        set(checkpoint) {
            if (!isOnline) return

            if (checkpoint == null) {
                disableCheckpoint()
                return
            }

            val loc = checkpoint.location
            SampNativeFunction.setPlayerCheckpoint(id, loc.x, loc.y, loc.z, checkpoint.size)
            field = checkpoint
        }
    override var raceCheckpoint: RaceCheckpoint? = null
        set(checkpoint) {
            if (!isOnline) return

            if (checkpoint == null) {
                disableRaceCheckpoint()
                return
            }

            val next = checkpoint.next
            val loc = checkpoint.location

            if (next != null) {
                val nextLoc = next.location
                SampNativeFunction.setPlayerRaceCheckpoint(id, checkpoint.type.value, loc.x, loc.y, loc.z,
                            nextLoc.x, nextLoc.y, nextLoc.z, checkpoint.size)
            } else {
                var type = checkpoint.type

                if (type == RaceCheckpointType.NORMAL) type = RaceCheckpointType.NORMAL_FINISH
                else if (type == RaceCheckpointType.AIR) type = RaceCheckpointType.AIR_FINISH
                    SampNativeFunction.setPlayerRaceCheckpoint(id, type.value, loc.x, loc.y, loc.z, loc.x, loc.y,
                            loc.z, checkpoint.size)
            }

            field = checkpoint
        }

    override val ping: Int
        get() {
            if (!isOnline) return 0

            return SampNativeFunction.getPlayerPing(id)
        }

    override var team: Int
        get() {
            if (!isOnline) return Player.NO_TEAM

            return SampNativeFunction.getPlayerTeam(id)
        }
        set(team) {
            if (!isOnline) return

            SampNativeFunction.setPlayerTeam(id, team)
        }

    override var skin: Int
        get() {
            if (!isOnline) return 0

            return SampNativeFunction.getPlayerSkin(id)
        }
        set(skin) {
            if (!isOnline) return

            SampNativeFunction.setPlayerSkin(id, skin)
        }

    override var wantedLevel: Int
        get() {
            if (!isOnline) return 0

            return SampNativeFunction.getPlayerWantedLevel(id)
        }
        set(level) {
            if (!isOnline) return

            SampNativeFunction.setPlayerWantedLevel(id, level)
        }

    override var codepage: Int
        get() {
            if (!isOnline) return 0

            return SampNativeFunction.getPlayerCodepage(id)
        }
        set(codepage) {
            if (!isOnline) return

            SampNativeFunction.setPlayerCodepage(id, codepage)
        }

    override val ip: String
        get() {
            if (!isOnline) return "0.0.0.0"

            return SampNativeFunction.getPlayerIp(id)
        }

    override var name: String
        get() {
            if (!isOnline) return "Unknown"

            return SampNativeFunction.getPlayerName(id)
        }
        @Throws(IllegalArgumentException::class, IllegalLengthException::class, AlreadyExistException::class)
        set(name) {
            if (!isOnline) return

            if (name.length < 3 || name.length > 20) throw IllegalLengthException()

            val ret = SampNativeFunction.setPlayerName(id, name)
            if (ret == 0) throw AlreadyExistException()
            if (ret == -1) throw IllegalArgumentException()
        }

    override var color: Color
        get() = Color(SampNativeFunction.getPlayerColor(id))
        set(color) = SampNativeFunction.setPlayerColor(id, color.value)

    override var health: Float
        get() {
            if (!isOnline) return 0.0f

            return SampNativeFunction.getPlayerHealth(id)
        }
        set(health) {
            if (!isOnline) return

            SampNativeFunction.setPlayerHealth(id, health)
        }

    override var armour: Float
        get() {
            if (!isOnline) return 0.0f

            return SampNativeFunction.getPlayerArmour(id)
        }
        set(armour) {
            if (!isOnline) return

            SampNativeFunction.setPlayerArmour(id, armour)
        }

    override var dialog: DialogId? = null

    private val eventManagerNode: EventManagerNode = eventManager.createChildNode()

    init {
        SampNativeFunction.getPlayerVelocity(id, velocity)
        SampNativeFunction.getPlayerKeys(id, keyState)
    }

    fun onPlayerUpdate() {
        updateCount++
        if (updateCount < 0) updateCount = 0
    }

    fun onPlayerDisconnect() {
        id = Player.INVALID_ID
    }

    fun onDialogResponse() {
        dialog = null
    }

    override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("id", id)
            .append("name", name)
            .append("state", state).toString()

    override var angle: Float
        get() = SampNativeFunction.getPlayerFacingAngle(id)
        set(angle) = SampNativeFunction.setPlayerFacingAngle(id, angle)

    override var location: AngledLocation
        get() {
            val location = AngledLocation()
            SampNativeFunction.getPlayerPos(id, location)
            location.angle = SampNativeFunction.getPlayerFacingAngle(id)
            location.interiorId = SampNativeFunction.getPlayerInterior(id)
            location.worldId = SampNativeFunction.getPlayerVirtualWorld(id)
            return location
        }
        set(value) {
            val vehicle = vehicle
            if (vehicle != null && vehicleSeat == 0) {
                vehicle.location = value
            } else {
                SampNativeFunction.setPlayerPos(id, value.x, value.y, value.z)
                SampNativeFunction.setPlayerInterior(id, value.interiorId)
                SampNativeFunction.setPlayerVirtualWorld(id, value.worldId)
                SampNativeFunction.setPlayerFacingAngle(id, value.angle)
            }
        }

    override var interior: Int
        get() = location.interiorId
        set(value) {
            val loc = location.clone()
            loc.interiorId = value
            location = loc
        }

    override var world: Int
        get() = location.worldId
        set(value) {
            val loc = location.clone()
            loc.worldId = value
            location = loc
        }

    override var facingAngle: Float
        get() = location.angle
        set(value) {
            val loc = location.clone()
            loc.angle = value
            location = loc
        }

    override fun getWeaponData(slot: Int): WeaponData? {
        val data = WeaponData()
        SampNativeFunction.getPlayerWeaponData(id, slot, data)
        return data
    }

    override var armedWeapon: WeaponModel
        get() = WeaponModel.get(SampNativeFunction.getPlayerWeapon(id)) ?: WeaponModel.UNKNOWN
        set(model) = SampNativeFunction.setPlayerArmedWeapon(id, model.id)

    override val armedWeaponAmmo: Int
        get() = SampNativeFunction.getPlayerAmmo(id)

    override var money: Int
        get() = SampNativeFunction.getPlayerMoney(id)
        set(money) {
            SampNativeFunction.resetPlayerMoney(id)
            if (money != 0) SampNativeFunction.givePlayerMoney(id, money)
        }

    override var score: Int
        get() = SampNativeFunction.getPlayerScore(id)
        set(score) = SampNativeFunction.setPlayerScore(id, score)

    override val cameraMode: Int
        get() = SampNativeFunction.getPlayerCameraMode(id)

    override val cameraAspectRatio: Float
        get() = SampNativeFunction.getPlayerCameraAspectRatio(id)

    override val cameraZoom: Float
        get() = SampNativeFunction.getPlayerCameraZoom(id)

    override var fightStyle: FightStyle
        get() = FightStyle.get(SampNativeFunction.getPlayerFightingStyle(id)) ?: FightStyle.NORMAL
        set(style) = SampNativeFunction.setPlayerFightingStyle(id, style.value)

    override var vehicle: Vehicle?
        get() = store.getVehicle(SampNativeFunction.getPlayerVehicleID(id))
        set(vehicle) {
            if(vehicle != null) setVehicle(vehicle, 0)
        }

    override val vehicleSeat: Int
        get() = SampNativeFunction.getPlayerVehicleSeat(id)

    override var specialAction: SpecialAction
        get() = SpecialAction.get(SampNativeFunction.getPlayerSpecialAction(id)) ?: SpecialAction.NONE
        set(action) = SampNativeFunction.setPlayerSpecialAction(id, action.value)

    override val state: PlayerState
        get() = PlayerState.values()[SampNativeFunction.getPlayerState(id)]

    override fun setWeaponAmmo(weapon: WeaponModel, ammo: Int) = SampNativeFunction.setPlayerAmmo(id, weapon.id, ammo)

    override fun giveMoney(money: Int) = SampNativeFunction.givePlayerMoney(id, money)

    override fun setLocation(x: Float, y: Float, z: Float) {
        val loc = location.clone()
        loc.set(x, y, z)
        location = loc
    }

    override fun setLocation(pos: Vector3D) = setLocation(pos.x, pos.y, pos.z)

    override fun setLocation(loc: Location) {
        val newLoc = location.clone()
        newLoc.set(loc)
        location = newLoc
    }

    override fun setLocationFindZ(x: Float, y: Float, z: Float) = setLocationFindZ(AngledLocation(x, y, z))
    override fun setLocationFindZ(pos: Vector3D) = setLocationFindZ(AngledLocation(pos))
    override fun setLocationFindZ(loc: Location) = setLocationFindZ(AngledLocation(loc))

    override fun setLocationFindZ(loc: AngledLocation) {
        SampNativeFunction.setPlayerPosFindZ(id, loc.x, loc.y, loc.z)
        SampNativeFunction.setPlayerFacingAngle(id, loc.angle)

        SampNativeFunction.setPlayerInterior(id, loc.interiorId)
        SampNativeFunction.setPlayerVirtualWorld(id, loc.worldId)
    }

    private fun sendMessageInternal(color: Color, message: String) {
        val msgs = CharsetUtils.splitStringByCharsetLength(message, 144, CharsetUtils.getCharsetByCodepage(codepage)!!, "  ")
        for (s in msgs) SampNativeFunction.sendClientMessage(id, color.value, s)
    }

    override fun sendMessage(color: Color, format: String, vararg args: Any) =
            sendMessageInternal(color, String.format(format, *args))

    override fun sendGameText(time: Int, style: Int, format: String, vararg args: Any) =
            SampNativeFunction.gameTextForPlayer(id, String.format(format, *args), time, style)

    override fun spawn() = SampNativeFunction.spawnPlayer(id)

    override var drunkLevel: Int
        get() = SampNativeFunction.getPlayerDrunkLevel(id)
        set(level) = SampNativeFunction.setPlayerDrunkLevel(id, level)

    override fun setVehicle(vehicle: Vehicle, seat: Int) = vehicle.putPlayer(this, seat)

    override fun clearAnimations(forcesync: Int) = SampNativeFunction.clearAnimations(id, forcesync)

    override val animationIndex: Int
        get() = SampNativeFunction.getPlayerAnimationIndex(id)

    override fun playSound(sound: Int, x: Float, y: Float, z: Float) =
            SampNativeFunction.playerPlaySound(id, sound, x, y, z)

    override fun playSound(sound: Int, pos: Vector3D) =
            SampNativeFunction.playerPlaySound(id, sound, pos.x, pos.y, pos.z)

    override fun playSound(sound: Int) = SampNativeFunction.playerPlaySound(id, sound, 0.0f, 0.0f, 0.0f)

    override fun markerForPlayer(player: Player, color: Color) =
            SampNativeFunction.setPlayerMarkerForPlayer(id, player.id, color.value)

    override fun showNameTagForPlayer(player: Player, show: Boolean) =
            SampNativeFunction.showPlayerNameTagForPlayer(id, player.id, show)

    override fun kick() = SampNativeFunction.kick(id)

    override fun ban() = SampNativeFunction.ban(id)

    override val currentMenu: Menu?
        get() = store.getMenu(SampNativeFunction.getPlayerMenu(id))

    override fun setCameraPosition(x: Float, y: Float, z: Float) = SampNativeFunction.setPlayerCameraPos(id, x, y, z)

    override fun setCameraLookAt(x: Float, y: Float, z: Float, cut: CameraCutStyle) =
            SampNativeFunction.setPlayerCameraLookAt(id, x, y, z, cut.value)

    override fun setCameraLookAt(lookAt: Vector3D, cut: CameraCutStyle) =
            setCameraLookAt(lookAt.x, lookAt.y, lookAt.z, cut)

    override fun setCameraLookAt(x: Float, y: Float, z: Float) = setCameraLookAt(x, y, z, CameraCutStyle.CUT)

    override fun setCameraLookAt(lookAt: Vector3D) = setCameraLookAt(lookAt.x, lookAt.y, lookAt.z, CameraCutStyle.CUT)

    override fun setCameraBehind() = SampNativeFunction.setCameraBehindPlayer(id)

    override var cameraPosition: Vector3D
        get() {
            val pos = Vector3D()
            SampNativeFunction.getPlayerCameraPos(id, pos)
            return pos
        }
        set(pos) = SampNativeFunction.setPlayerCameraPos(id, pos.x, pos.y, pos.z)

    override val cameraFrontVector: Vector3D
        get() {
            val lookAt = Vector3D()
            SampNativeFunction.getPlayerCameraFrontVector(id, lookAt)
            return lookAt
        }

    override val isInAnyVehicle: Boolean
        get() = isOnline && SampNativeFunction.isPlayerInAnyVehicle(id)

    override fun isInVehicle(veh: Vehicle): Boolean {
        return isOnline && !veh.isDestroyed && SampNativeFunction.isPlayerInVehicle(id, veh.id)
    }

    override val isAdmin: Boolean
        get() = isOnline && SampNativeFunction.isPlayerAdmin(id)

    override fun isStreamedIn(forPlayer: Player): Boolean {
        return isOnline && forPlayer.isOnline && SampNativeFunction.isPlayerStreamedIn(id, forPlayer.id)
    }

    override val isNpc: Boolean
        get() = isOnline && SampNativeFunction.isPlayerNPC(id)

    override fun disableCheckpoint() {
        SampNativeFunction.disablePlayerCheckpoint(id)
        checkpoint = null
    }

    override fun disableRaceCheckpoint() {
        SampNativeFunction.disablePlayerRaceCheckpoint(id)
        raceCheckpoint = null
    }

    override val weaponState: WeaponState
        get() = WeaponState.get(SampNativeFunction.getPlayerWeaponState(id)) ?: WeaponState.UNKNOWN

    override fun giveWeapon(type: WeaponModel, ammo: Int) = SampNativeFunction.givePlayerWeapon(id, type.id, ammo)

    override fun giveWeapon(data: WeaponData) = giveWeapon(data.model, data.ammo)

    override fun resetWeapons() = SampNativeFunction.resetPlayerWeapons(id)

    override var time: Time
        get() {
            val time = Time()
            SampNativeFunction.getPlayerTime(id, time)
            return time
        }
        set(time) = SampNativeFunction.setPlayerTime(id, time.hour, time.minute)

    override fun toggleClock(toggle: Boolean) = SampNativeFunction.togglePlayerClock(id, toggle)

    override fun forceClassSelection() = SampNativeFunction.forceClassSelection(id)

    override fun playCrimeReport(suspectId: Int, crimeId: Int) =
            SampNativeFunction.playCrimeReportForPlayer(id, suspectId, crimeId)

    override fun setShopName(shop: ShopName) = SampNativeFunction.setPlayerShopName(id, shop.value)

    override val surfingVehicle: Vehicle?
        get() = store.getVehicle(SampNativeFunction.getPlayerSurfingVehicleID(id))

    override fun removeFromVehicle() = SampNativeFunction.removePlayerFromVehicle(id)

    override fun toggleControllable(toggle: Boolean) {
        SampNativeFunction.togglePlayerControllable(id, toggle)
        isControllable = toggle
    }

    override fun enableStuntBonus(enabled: Boolean) {
        SampNativeFunction.enableStuntBonusForPlayer(id, if (enabled) 1 else 0)
        isStuntBonusEnabled = enabled
    }

    override fun toggleSpectating(toggle: Boolean) {
        SampNativeFunction.togglePlayerSpectating(id, toggle)
        isSpectating = toggle

        if (toggle) return

        spectatingPlayer = null
        spectatingVehicle = null
    }

    override fun spectate(player: Player, mode: SpectateMode) {
        if (!isSpectating) return

        SampNativeFunction.playerSpectatePlayer(id, player.id, mode.value)
        spectatingPlayer = player
        spectatingVehicle = null
    }

    override fun spectate(veh: Vehicle, mode: SpectateMode) {
        if (!isSpectating) return

        SampNativeFunction.playerSpectateVehicle(id, veh.id, mode.value)
        spectatingPlayer = null
        spectatingVehicle = vehicle
    }

    override fun startRecord(type: RecordType, recordName: String) {
        SampNativeFunction.startRecordingPlayerData(id, type.value, recordName)
        isRecording = true
    }

    override fun stopRecord() {
        SampNativeFunction.stopRecordingPlayerData(id)
        isRecording = false
    }

    override val surfingObject: SampObject?
        get() {
            val objectid = SampNativeFunction.getPlayerSurfingObjectID(id)
            if (objectid == SampObject.INVALID_ID) return null
            return store.getObject(objectid)
        }

    override val networkStats: String
        get() = SampNativeFunction.getPlayerNetworkStats(id)

    override val aimedTarget: Player?
        get() = store.getPlayer(SampNativeFunction.getPlayerTargetPlayer(id))

    override fun playAudioStream(url: String) =
            SampNativeFunction.playAudioStreamForPlayer(id, url, 0.0f, 0.0f, 0.0f, 0.0f, 0)

    override fun playAudioStream(url: String, x: Float, y: Float, z: Float, distance: Float) =
            SampNativeFunction.playAudioStreamForPlayer(id, url, x, y, z, distance, 1)

    override fun playAudioStream(url: String, location: Vector3D, distance: Float) =
            playAudioStream(url, location.x, location.y, location.z, distance)

    override fun playAudioStream(url: String, loc: Radius) =
            playAudioStream(url, loc.x, loc.y, loc.z, loc.radius)

    override fun stopAudioStream() = SampNativeFunction.stopAudioStreamForPlayer(id)

    override fun removeBuilding(modelId: Int, x: Float, y: Float, z: Float, radius: Float) =
            SampNativeFunction.removeBuildingForPlayer(id, modelId, x, y, z, radius)

    override val lastShotOrigin: Vector3D
        get() {
            val origin = Vector3D()
            val hitpos = Vector3D()

            SampNativeFunction.getPlayerLastShotVectors(id, origin, hitpos)
            return origin
        }

    override val lastShotHitPosition: Vector3D
        get() {
            val origin = Vector3D()
            val hitpos = Vector3D()

            SampNativeFunction.getPlayerLastShotVectors(id, origin, hitpos)
            return hitpos
        }

    override fun removeBuilding(modelId: Int, pos: Vector3D, radius: Float) =
            removeBuilding(modelId, pos.x, pos.y, pos.z, radius)

    override fun removeBuilding(modelId: Int, loc: Radius) =
            removeBuilding(modelId, loc.x, loc.y, loc.z, loc.radius)

    override fun showDialog(dialog: DialogId, style: DialogStyle, caption: String, text: String,
                            button1: String, button2: String) {
        if (this.dialog != null) {
            DialogEventUtils.dispatchCloseEvent(eventManagerNode, this.dialog!!, this, DialogCloseType.OVERRIDE)
        }

        SampNativeFunction.showPlayerDialog(id, dialog.id, style.value, caption, text, button1, button2)
        this.dialog = dialog

        DialogEventUtils.dispatchShowEvent(eventManagerNode, dialog, this)
    }

    override fun cancelDialog() {
        if (dialog == null) return

        SampNativeFunction.showPlayerDialog(id, DialogIdImpl.INVALID_ID, 0, " ", " ", " ", " ")
        DialogEventUtils.dispatchCloseEvent(eventManagerNode, dialog!!, this, DialogCloseType.CANCEL)

        dialog = null
    }

    override fun editObject(`object`: SampObject): Boolean =
            isOnline && !`object`.isDestroyed && SampNativeFunction.editObject(id, `object`.id)

    override fun editPlayerObject(`object`: PlayerObject): Boolean =
            isOnline && !`object`.isDestroyed && SampNativeFunction.editPlayerObject(id, `object`.id)

    override fun selectObject() = SampNativeFunction.selectObject(id)

    override fun cancelEdit() = SampNativeFunction.cancelEdit(id)

    override fun attachCameraTo(`object`: SampObject) {
        if (`object`.isDestroyed) return
        SampNativeFunction.attachCameraToObject(id, `object`.id)
    }

    override fun attachCameraTo(`object`: PlayerObject) {
        if (`object`.isDestroyed) return
        if (`object`.player !== this) return

        SampNativeFunction.attachCameraToPlayerObject(id, `object`.id)
    }

    override fun interpolateCameraPosition(fromX: Float, fromY: Float, fromZ: Float, toX: Float, toY: Float,
                                           toZ: Float, time: Int, cut: CameraCutStyle) =
            SampNativeFunction.interpolateCameraPos(id, fromX, fromY, fromZ, toX, toY, toZ, time, cut.value)

    override fun interpolateCameraPosition(from: Vector3D, to: Vector3D, time: Int, cut: CameraCutStyle) =
            interpolateCameraPosition(from.x, from.y, from.z, to.x, to.y, to.z, time, cut)

    override fun interpolateCameraLookAt(fromX: Float, fromY: Float, fromZ: Float, toX: Float, toY: Float, toZ: Float,
                                         time: Int, cut: CameraCutStyle) =
            SampNativeFunction.interpolateCameraLookAt(id, fromX, fromY, fromZ, toX, toY, toZ, time, cut.value)

    override fun interpolateCameraLookAt(from: Vector3D, to: Vector3D, time: Int, cut: CameraCutStyle) =
            interpolateCameraLookAt(from.x, from.y, from.z, to.x, to.y, to.z, time, cut)

    override fun selectTextDraw(hoverColor: Color) = SampNativeFunction.selectTextDraw(id, hoverColor.value)

    override fun cancelSelectTextDraw() = SampNativeFunction.cancelSelectTextDraw(id)

    override fun createExplosion(x: Float, y: Float, z: Float, type: Int, radius: Float) =
            SampNativeFunction.createExplosionForPlayer(id, x, y, z, type, radius)

    override val version: String
        get() = SampNativeFunction.getPlayerVersion(id)

    override val mainZoneName: LocationZone?
        get() = LocationZone.getPlayerMainZone(this)

    override val zoneName: LocationZone?
        get() = LocationZone.getPlayerZone(this)

    override val connectedTime: Int
        get() = SampNativeFunction.netStats_GetConnectedTime(id)

    override val messagesReceived: Int
        get() = SampNativeFunction.netStats_MessagesReceived(id)

    override val bytesReceived: Int
        get() = SampNativeFunction.netStats_BytesReceived(id)

    override val messagesSent: Int
        get() = SampNativeFunction.netStats_MessagesSent(id)

    override val bytesSent: Int
        get() = SampNativeFunction.netStats_BytesSent(id)

    override val messagesRecvPerSecond: Int
        get() = SampNativeFunction.netStats_MessagesRecvPerSecond(id)

    override val packetLossPercent: Float
        get() = SampNativeFunction.netStats_PacketLossPercent(id)

    override val connectionStatus: Int
        get() = SampNativeFunction.netStats_ConnectionStatus(id)

    override val ipPort: String
        get() = SampNativeFunction.netStats_GetIpPort(id)

    override fun setChatBubble(text: String, color: Color, drawDistance: Float, expireTime: Int) =
            SampNativeFunction.setPlayerChatBubble(id, text, color.value, drawDistance, expireTime)

    override fun setVarInt(name: String, value: Int) = SampNativeFunction.setPVarInt(id, name, value)

    override fun getVarInt(name: String): Int = SampNativeFunction.getPVarInt(id, name)

    override fun setVarString(name: String, value: String) = SampNativeFunction.setPVarString(id, name, value)

    override fun getVarString(name: String): String = SampNativeFunction.getPVarString(id, name)

    override fun setVarFloat(name: String, value: Float) = SampNativeFunction.setPVarFloat(id, name, value)

    override fun getVarFloat(name: String): Float = SampNativeFunction.getPVarFloat(id, name)

    override fun deleteVar(name: String): Boolean = SampNativeFunction.deletePVar(id, name) != 0

    override val varNames: List<String>
        get() {
            val names = mutableListOf<String>()
            (0..SampNativeFunction.getPVarsUpperIndex(id)).forEach { i ->
                names.add(SampNativeFunction.getPVarNameAtIndex(id, i))
            }
            return names
        }

    override fun getVarType(name: String): PlayerVarType? = PlayerVarType.get(SampNativeFunction.getPVarType(id, name))

    override fun disableRemoteVehicleCollisions(disable: Boolean) =
            SampNativeFunction.disableRemoteVehicleCollisions(id, if (disable) 1 else 0)

    override fun enablePlayerCameraTarget(enable: Boolean) {
        SampNativeFunction.enablePlayerCameraTarget(id, if (enable) 1 else 0)
    }

    override val cameraTargetActor: Actor?
        get() {
            val actorId = SampNativeFunction.getPlayerCameraTargetActor(id)
            if (actorId != Actor.INVALID_ACTOR) return Actor.get(actorId)
            return null
        }

    override val cameraTargetObject: SampObject?
        get() {
            val objectId = SampNativeFunction.getPlayerCameraTargetObject(id)
            if (objectId != SampObject.INVALID_ID) return SampObject.get(objectId)
            return null
        }

    override val cameraTargetPlayer: Player?
        get() {
            val playerId = SampNativeFunction.getPlayerCameraTargetPlayer(id)
            if (playerId != Player.INVALID_ID) return Player.get(playerId)
            return null
        }

    override val cameraTargetVehicle: Vehicle?
        get() {
            val vehicleId = SampNativeFunction.getPlayerCameraTargetVehicle(id)
            if (vehicleId != Vehicle.INVALID_ID) return Vehicle.get(vehicleId)
            return null
        }

    override val targetActor: Actor?
        get() {
            val actorId = SampNativeFunction.getPlayerTargetActor(id)
            if (actorId != Actor.INVALID_ACTOR) return Actor.get(actorId)
            return null
        }

    override fun applyAnimation(animlib: String, animname: String, delta: Float, loop: Int, lockX: Int, lockY: Int,
                                freeze: Int, time: Int, forcesync: Int) =
            SampNativeFunction.applyAnimation(id, animlib, animname, delta, loop, lockX, lockY, freeze, time, forcesync)

    override fun ban(reason: String) = SampNativeFunction.banEx(id, reason)

    override fun sendChat(player: Player, message: String) =
            SampNativeFunction.sendPlayerMessageToPlayer(player.id, id, message)

    override fun sendChatToAll(message: String) =
            store.players.filter { it != this }.forEach { it.sendChat(this, message) }

    override fun sendDeathMessage(killer: Player?, victim: Player?, weapon: WeaponModel) =
            SampNativeFunction.sendDeathMessageToPlayer(id, killer?.id ?: INVALID_ID, victim?.id ?: INVALID_ID,
                    weapon.id)

    override fun sendGameText(time: Int, style: Int, text: String) =
            SampNativeFunction.gameTextForPlayer(id, text, time, style)

    override fun sendMessage(color: Color, message: String) = sendMessageInternal(color, message)

    override fun setSpawnInfo(x: Float, y: Float, z: Float, interiorId: Int, worldId: Int, angle: Float, skinId: Int,
                              teamId: Int, weapon1: WeaponData, weapon2: WeaponData, weapon3: WeaponData) =
            SampNativeFunction.setSpawnInfo(id, teamId, skinId, x, y, z, angle, weapon1.model.id, weapon1.ammo,
                                      weapon2.model.id, weapon2.ammo, weapon3.model.id, weapon3.ammo)
}
