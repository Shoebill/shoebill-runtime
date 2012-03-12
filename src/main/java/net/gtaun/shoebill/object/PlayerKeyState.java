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

package net.gtaun.shoebill.object;

import net.gtaun.shoebill.samp.SampNativeFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 *
 */

public class PlayerKeyState
{
	enum Key
	{
		ACTION					(1),
		CROUCH					(2),
		FIRE					(4),
		SPRINT					(8),
		SECONDARY_ATTACK		(16),
		JUMP					(32),
	    LOOK_RIGHT				(64),
	    HANDBRAKE				(128),
	    LOOK_LEFT				(256),
		SUBMISSION				(512),
	    LOOK_BEHIND				(512),
	    WALK					(1024),
	    ANALOG_UP				(2048),
	    ANALOG_DOWN				(4096),
	    ANALOG_LEFT				(8192),
	    ANALOG_RIGHT			(16384);
		
		public final int value;
		Key( int val )		{ value = val; }
	}
	
	public static final int KEY_UP				= -128;
	public static final int KEY_DOWN			= 128;
	public static final int KEY_LEFT			= -128;
    public static final int KEY_RIGHT			= 128;

    
    private IPlayer player;
    private int keys, updown, leftright;
    
    
	public PlayerKeyState( IPlayer player )
	{
		this.player = player;
	}
	
	void update()
	{
		if( player.isOnline() == false ) return;
		
		SampNativeFunction.getPlayerKeys( player.getId(), this );
	}

	public IPlayer getPlayer()
	{
		return player;
	}

	public int getKeys()
	{
		return keys;
	}

	public int getUpdown()
	{
		return updown;
	}

	public int getLeftright()
	{
		return leftright;
	}

	public boolean isKeyPressed( Key key )
	{
		return (keys&key.value) != 0;
	}
	
	@Override
	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		builder.append( "keys",			keys );
		builder.append( "upDown",		updown );
		builder.append( "leftRight",	leftright );
		builder.append( "action",		isKeyPressed(Key.ACTION)			? 1 : 0 );
		builder.append( "crouch",		isKeyPressed(Key.CROUCH)			? 1 : 0 );
		builder.append( "fire",			isKeyPressed(Key.FIRE)				? 1 : 0 );
		builder.append( "sprint",		isKeyPressed(Key.SPRINT)			? 1 : 0 );
		builder.append( "secondAttack",	isKeyPressed(Key.SECONDARY_ATTACK)	? 1 : 0 );
		builder.append( "jump",			isKeyPressed(Key.JUMP)				? 1 : 0 );
		builder.append( "lookRight",	isKeyPressed(Key.LOOK_RIGHT)		? 1 : 0 );
		builder.append( "handbreak",	isKeyPressed(Key.HANDBRAKE)			? 1 : 0 );
		builder.append( "lookLeft",		isKeyPressed(Key.LOOK_LEFT)			? 1 : 0 );
		builder.append( "subMission",	isKeyPressed(Key.SUBMISSION)		? 1 : 0 );
		builder.append( "lookBehind",	isKeyPressed(Key.LOOK_BEHIND)		? 1 : 0 );
		builder.append( "walk",			isKeyPressed(Key.WALK)				? 1 : 0 );
		builder.append( "analogUp",		isKeyPressed(Key.ANALOG_UP)			? 1 : 0 );
		builder.append( "analogDown",	isKeyPressed(Key.ANALOG_DOWN)		? 1 : 0 );
		builder.append( "analogLeft",	isKeyPressed(Key.ANALOG_LEFT)		? 1 : 0 );
		builder.append( "analogRight",	isKeyPressed(Key.ANALOG_RIGHT)		? 1 : 0 );
	    
		return builder.build();
	}
}
