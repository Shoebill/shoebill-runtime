/**
 * Copyright (C) 2011-2014 MK124

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

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.SampObjectStore
import net.gtaun.shoebill.constant.ServerVarType
import net.gtaun.shoebill.constant.VehicleModelInfoType
import net.gtaun.shoebill.constant.WeaponModel
import net.gtaun.shoebill.data.Animation
import net.gtaun.shoebill.data.Color
import net.gtaun.shoebill.data.Vector3D
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.Server
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author MK124
 * @author Marvin Haschker
 */
class ServerImpl(private val store: SampObjectStore) : Server() {

    override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).toString()

    override var serverCodepage: Int
        get() = SampNativeFunction.getServerCodepage()
        set(codepage) = SampNativeFunction.setServerCodepage(codepage)

    override val maxPlayers: Int
        get() = SampNativeFunction.getMaxPlayers()

    override var gamemodeText: String
        get() = SampNativeFunction.getConsoleVarAsString("gamemodetext")
        set(text) = SampNativeFunction.setGameModeText(text)

    override fun sendRconCommand(command: String) = SampNativeFunction.sendRconCommand(command)

    override fun connectNPC(name: String, script: String) = SampNativeFunction.connectNPC(name, script)

    override fun sendMessageToAll(color: Color, message: String) =
            store.players.forEach { it.sendMessage(color, message) }

    override fun sendMessageToAll(color: Color, format: String, vararg args: Any) =
            sendMessageToAll(color, String.format(format, *args))

    override fun sendMessageToAll(message: String) = sendMessageToAll(Color.WHITE, message)

    override fun sendMessageToAll(format: String, vararg args: Any) = sendMessageToAll(Color.WHITE, format, *args)

    override fun gameTextToAll(time: Int, style: Int, text: String) =
            SampNativeFunction.gameTextForAll(text, time, style)

    override fun gameTextToAll(time: Int, style: Int, format: String, vararg args: Any) =
            SampNativeFunction.gameTextForAll(String.format(format, *args), time, style)

    override fun sendDeathMessageToAll(killer: Player?, victim: Player?, reason: WeaponModel) =
            SampNativeFunction.sendDeathMessage(if (killer != null) killer.id else Player.INVALID_ID,
                    if (victim != null) victim.id else Player.INVALID_ID, reason.id)

    override fun getVehicleModelInfo(modelId: Int, infotype: VehicleModelInfoType): Vector3D {
        val vector = Vector3D()
        SampNativeFunction.getVehicleModelInfo(modelId, infotype.value, vector)
        return vector
    }

    override fun blockIpAddress(ipAddress: String, timeMs: Int) = SampNativeFunction.blockIpAddress(ipAddress, timeMs)

    override fun unBlockIpAddress(ipAddress: String) = SampNativeFunction.unBlockIpAddress(ipAddress)

    override var hostname: String
        get() = SampNativeFunction.getConsoleVarAsString("hostname")
        set(name) = SampNativeFunction.sendRconCommand("hostname " + name)

    override var mapname: String
        get() = SampNativeFunction.getConsoleVarAsString("mapname")
        set(name) = SampNativeFunction.sendRconCommand("mapname " + name)

    override var password: String
        get() = SampNativeFunction.getConsoleVarAsString("password")
        set(password) = SampNativeFunction.sendRconCommand("password " + password)

    override fun getAnimationName(animationIndex: Int): Animation {
        val animationInfo = SampNativeFunction.getAnimationName(animationIndex)
        return Animation(animationInfo[0], animationInfo[1])
    }

    override fun hashPassword(password: String, salt: String): String = SampNativeFunction.sha256Hash(password, salt)

    override fun getConsoleIntVar(varname: String): Int = SampNativeFunction.getConsoleVarAsInt(varname)

    override fun getConsoleBoolVar(varname: String): Boolean = SampNativeFunction.getConsoleVarAsBool(varname)

    override fun getConsoleStringVar(varname: String): String = SampNativeFunction.getConsoleVarAsString(varname)

    override fun setIntVar(varname: String, value: Int) = SampNativeFunction.setServerVarInt(varname, value)

    override fun setFloatVar(varname: String, value: Float) = SampNativeFunction.setServerVarFloat(varname, value)

    override fun setStringVar(varname: String, value: String) = SampNativeFunction.setServerVarString(varname, value)

    override fun getIntVar(varname: String): Int = SampNativeFunction.getServerVarAsInt(varname)

    override fun getFloatVar(varname: String): Float = SampNativeFunction.getServerVarAsFloat(varname)

    override fun getStringVar(varname: String): String = SampNativeFunction.getServerVarAsString(varname)

    override fun deleteVar(varname: String): Boolean = SampNativeFunction.deleteServerVar(varname)

    override val varsUpperIndex: Int
        get() = SampNativeFunction.getServerVarsUpperIndex()

    override fun getVarNameAtIndex(index: Int): String = SampNativeFunction.getServerVarNameAtIndex(index)

    override fun getVarType(varname: String): ServerVarType? =
            ServerVarType.get(SampNativeFunction.getServerVarType(varname))
}
