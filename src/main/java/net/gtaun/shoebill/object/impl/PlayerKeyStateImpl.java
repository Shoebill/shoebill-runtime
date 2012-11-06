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

import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerKeyState;
import net.gtaun.shoebill.samp.SampNativeFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public class PlayerKeyStateImpl implements PlayerKeyState
{
	private Player player;
	private int keys, updown, leftright;
	
	
	public PlayerKeyStateImpl(Player player)
	{
		this.player = player;
	}
	
	void update()
	{
		if (player.isOnline() == false) return;
		
		SampNativeFunction.getPlayerKeys(player.getId(), this);
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public int getKeys()
	{
		return keys;
	}
	
	@Override
	public int getUpdown()
	{
		return updown;
	}
	
	@Override
	public int getLeftright()
	{
		return leftright;
	}
	
	@Override
	public boolean isKeyPressed(Key key)
	{
		return (keys & key.getValue()) != 0;
	}
	
	@Override
	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		builder.append("keys", keys);
		builder.append("upDown", updown);
		builder.append("leftRight", leftright);
		builder.append("action", isKeyPressed(Key.ACTION) ? 1 : 0);
		builder.append("crouch", isKeyPressed(Key.CROUCH) ? 1 : 0);
		builder.append("fire", isKeyPressed(Key.FIRE) ? 1 : 0);
		builder.append("sprint", isKeyPressed(Key.SPRINT) ? 1 : 0);
		builder.append("secondAttack", isKeyPressed(Key.SECONDARY_ATTACK) ? 1 : 0);
		builder.append("jump", isKeyPressed(Key.JUMP) ? 1 : 0);
		builder.append("lookRight", isKeyPressed(Key.LOOK_RIGHT) ? 1 : 0);
		builder.append("handbreak", isKeyPressed(Key.HANDBRAKE) ? 1 : 0);
		builder.append("lookLeft", isKeyPressed(Key.LOOK_LEFT) ? 1 : 0);
		builder.append("subMission", isKeyPressed(Key.SUBMISSION) ? 1 : 0);
		builder.append("lookBehind", isKeyPressed(Key.LOOK_BEHIND) ? 1 : 0);
		builder.append("walk", isKeyPressed(Key.WALK) ? 1 : 0);
		builder.append("analogUp", isKeyPressed(Key.ANALOG_UP) ? 1 : 0);
		builder.append("analogDown", isKeyPressed(Key.ANALOG_DOWN) ? 1 : 0);
		builder.append("analogLeft", isKeyPressed(Key.ANALOG_LEFT) ? 1 : 0);
		builder.append("analogRight", isKeyPressed(Key.ANALOG_RIGHT) ? 1 : 0);
		
		return builder.build();
	}
}
