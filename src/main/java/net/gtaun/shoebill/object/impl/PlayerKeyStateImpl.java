/**
 * Copyright (C) 2011 MK124
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
import net.gtaun.shoebill.constant.PlayerKey;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerKeyState;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 */
public class PlayerKeyStateImpl implements PlayerKeyState {
    private Player player;
    private int keys, updownValue, leftrightValue;


    public PlayerKeyStateImpl(Player player) {
        this(player, 0);
    }

    public PlayerKeyStateImpl(Player player, int keys) {
        this.player = player;
        this.keys = keys;
    }

    void update() {
        if (!player.isOnline()) return;

        SampNativeFunction.getPlayerKeys(player.getId(), this);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getKeys() {
        return keys;
    }

    @Override
    public int getUpdownValue() {
        return updownValue;
    }

    @Override
    public int getLeftrightValue() {
        return leftrightValue;
    }

    @Override
    public boolean isKeyPressed(PlayerKey... playerKeys) {
        int value = 0;
        for (PlayerKey key : playerKeys) value |= key.getValue();
        return (keys & value) == value;
    }

    @Override
    public boolean isAccurateKeyPressed(PlayerKey... playerKeys) {
        int value = 0;
        for (PlayerKey key : playerKeys) value |= key.getValue();
        return keys == value;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
        builder.append("keys", keys);
        builder.append("upDown", updownValue);
        builder.append("leftRight", leftrightValue);
        builder.append("action", isKeyPressed(PlayerKey.ACTION) ? 1 : 0);
        builder.append("crouch", isKeyPressed(PlayerKey.CROUCH) ? 1 : 0);
        builder.append("fire", isKeyPressed(PlayerKey.FIRE) ? 1 : 0);
        builder.append("sprint", isKeyPressed(PlayerKey.SPRINT) ? 1 : 0);
        builder.append("secondAttack", isKeyPressed(PlayerKey.SECONDARY_ATTACK) ? 1 : 0);
        builder.append("jump", isKeyPressed(PlayerKey.JUMP) ? 1 : 0);
        builder.append("lookRight", isKeyPressed(PlayerKey.LOOK_RIGHT) ? 1 : 0);
        builder.append("handbreak", isKeyPressed(PlayerKey.HANDBRAKE) ? 1 : 0);
        builder.append("lookLeft", isKeyPressed(PlayerKey.LOOK_LEFT) ? 1 : 0);
        builder.append("subMission", isKeyPressed(PlayerKey.SUBMISSION) ? 1 : 0);
        builder.append("lookBehind", isKeyPressed(PlayerKey.LOOK_BEHIND) ? 1 : 0);
        builder.append("walk", isKeyPressed(PlayerKey.WALK) ? 1 : 0);
        builder.append("analogUp", isKeyPressed(PlayerKey.ANALOG_UP) ? 1 : 0);
        builder.append("analogDown", isKeyPressed(PlayerKey.ANALOG_DOWN) ? 1 : 0);
        builder.append("analogLeft", isKeyPressed(PlayerKey.ANALOG_LEFT) ? 1 : 0);
        builder.append("analogRight", isKeyPressed(PlayerKey.ANALOG_RIGHT) ? 1 : 0);

        return builder.build();
    }
}
