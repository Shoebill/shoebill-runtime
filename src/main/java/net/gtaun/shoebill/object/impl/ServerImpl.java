/**
 * Copyright (C) 2011-2014 MK124
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.SampObjectStore;
import net.gtaun.shoebill.constant.VehicleModelInfoType;
import net.gtaun.shoebill.constant.WeaponModel;
import net.gtaun.shoebill.data.Animation;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.Server;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 */
public class ServerImpl implements Server {
    private final SampObjectStore store;

    public ServerImpl(SampObjectStore store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).toString();
    }

    @Override
    public int getServerCodepage() {
        return SampNativeFunction.getServerCodepage();
    }

    @Override
    public void setServerCodepage(int codepage) {
        SampNativeFunction.setServerCodepage(codepage);
    }

    @Override
    public int getMaxPlayers() {
        return SampNativeFunction.getMaxPlayers();
    }

    @Override
    public String getGamemodeText() {
        return SampNativeFunction.getConsoleVarAsString("gamemodetext");
    }

    @Override
    public void setGamemodeText(String text) {
        SampNativeFunction.setGameModeText(text);
    }

    @Override
    public void sendRconCommand(String command) {
        if (command == null) throw new NullPointerException();
        SampNativeFunction.sendRconCommand(command);
    }

    @Override
    public void connectNPC(String name, String script) {
        if (name == null || script == null) throw new NullPointerException();
        SampNativeFunction.connectNPC(name, script);
    }

    @Override
    public void sendMessageToAll(Color color, String message) {
        for (Player player : store.getPlayers()) {
            player.sendMessage(color, message);
        }
    }

    @Override
    public void sendMessageToAll(Color color, String format, Object... args) {
        String message = String.format(format, args);
        for (Player player : store.getPlayers()) {
            player.sendMessage(color, message);
        }
    }

    @Override
    public void sendMessageToAll(String message) {
        sendMessageToAll(Color.WHITE, message);
    }

    @Override
    public void sendMessageToAll(String format, Object... args) {
        sendMessageToAll(Color.WHITE, format, args);
    }

    @Override
    public void gameTextToAll(int time, int style, String text) {
        SampNativeFunction.gameTextForAll(text, time, style);
    }

    @Override
    public void gameTextToAll(int time, int style, String format, Object... args) {
        String text = String.format(format, args);
        SampNativeFunction.gameTextForAll(text, time, style);
    }

    @Override
    public void sendDeathMessageToAll(Player killer, Player victim, WeaponModel reason) {
        SampNativeFunction.sendDeathMessage(killer != null ? killer.getId() : PlayerImpl.INVALID_ID, victim != null ? victim.getId() : PlayerImpl.INVALID_ID, reason.getId());
    }

    @Override
    public Vector3D getVehicleModelInfo(int modelId, VehicleModelInfoType infotype) {
        Vector3D vector = new Vector3D();
        SampNativeFunction.getVehicleModelInfo(modelId, infotype.getValue(), vector);
        return vector;
    }

    @Override
    public void blockIpAddress(String ipAddress, int timeMs) {
        SampNativeFunction.blockIpAddress(ipAddress, timeMs);
    }

    @Override
    public void unBlockIpAddress(String ipAddress) {
        SampNativeFunction.unBlockIpAddress(ipAddress);
    }

    @Override
    public String getHostname() {
        return SampNativeFunction.getConsoleVarAsString("hostname");
    }

    @Override
    public void setHostname(String name) {
        SampNativeFunction.sendRconCommand("hostname " + name);
    }

    @Override
    public String getMapname() {
        return SampNativeFunction.getConsoleVarAsString("mapname");
    }

    @Override
    public void setMapname(String name) {
        SampNativeFunction.sendRconCommand("mapname " + name);
    }

    @Override
    public String getPassword() {
        return SampNativeFunction.getConsoleVarAsString("password");
    }

    @Override
    public void setPassword(String password) {
        SampNativeFunction.sendRconCommand("password " + password);
    }

    @Override
    public Animation getAnimationName(int animationIndex) {
        String[] animationInfo = SampNativeFunction.getAnimationName(animationIndex);
        return new Animation(animationInfo[0], animationInfo[1]);
    }

    @Override
    public String hashPassword(String password, String salt) {
        return SampNativeFunction.sha256Hash(password, salt);
    }

    @Override
    public int getIntVar(String varname) {
        return SampNativeFunction.getConsoleVarAsInt(varname);
    }

    @Override
    public boolean getBoolVar(String varname) {
        return SampNativeFunction.getConsoleVarAsBool(varname);
    }

    @Override
    public String getStringVar(String varname) {
        return SampNativeFunction.getConsoleVarAsString(varname);
    }
}
