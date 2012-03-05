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

package net.gtaun.shoebill.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 *
 */

@SuppressWarnings("unused")
public class KeyState implements Cloneable, Serializable
{
	private static final long serialVersionUID = 2902208210862136365L;
	
	
	private static final int KEY_ACTION					= 1;
	private static final int KEY_CROUCH					= 2;
	private static final int KEY_FIRE					= 4;
	private static final int KEY_SPRINT					= 8;
	private static final int KEY_SECONDARY_ATTACK		= 16;
	private static final int KEY_JUMP					= 32;
    private static final int KEY_LOOK_RIGHT				= 64;
    private static final int KEY_HANDBRAKE				= 128;
    private static final int KEY_LOOK_LEFT				= 256;
	private static final int KEY_SUBMISSION				= 512;
    private static final int KEY_LOOK_BEHIND			= 512;
    private static final int KEY_WALK					= 1024;
    private static final int KEY_ANALOG_UP				= 2048;
    private static final int KEY_ANALOG_DOWN			= 4096;
    private static final int KEY_ANALOG_LEFT			= 8192;
    private static final int KEY_ANALOG_RIGHT			= 16384;
    
    private static final int KEY_UP						= -128;
    private static final int KEY_DOWN					= 128;
    private static final int KEY_LEFT					= -128;
    private static final int KEY_RIGHT					= 128;

    
    private int keys, updown, leftright;
    
    
    public KeyState()
    {
    	
    }
    

    public boolean isActionPressed()			{ return (keys&KEY_ACTION) != 0; }
    public boolean isCrouchPressed()			{ return (keys&KEY_CROUCH) != 0; }
    public boolean isFirePressed()				{ return (keys&KEY_FIRE) != 0; }
    public boolean isSprintPressed()			{ return (keys&KEY_SPRINT) != 0; }
    public boolean isSecondAttackPressed()		{ return (keys&KEY_SECONDARY_ATTACK) != 0; }
    public boolean isJumpPressed()				{ return (keys&KEY_JUMP) != 0; }
    public boolean isLookRightPressed()			{ return (keys&KEY_LOOK_RIGHT) != 0; }
    public boolean isHandBreakPressed()			{ return (keys&KEY_HANDBRAKE) != 0; }
    public boolean isLookLeftPressed()			{ return (keys&KEY_LOOK_LEFT) != 0; }
    public boolean isSubMissionPressed()		{ return (keys&KEY_SUBMISSION) != 0; }
    public boolean isLookBehindPressed()		{ return (keys&KEY_LOOK_BEHIND) != 0; }
    public boolean isWalkPressed()				{ return (keys&KEY_WALK) != 0; }
    public boolean isAnalogUpPressed()			{ return (keys&KEY_ANALOG_UP) != 0; }
    public boolean isAnalogDownPressed()		{ return (keys&KEY_ANALOG_DOWN) != 0; }
    public boolean isAnalogLeftPressed()		{ return (keys&KEY_ANALOG_LEFT) != 0; }
    public boolean isAnalogRightPressed()		{ return (keys&KEY_ANALOG_RIGHT) != 0; }
    
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(217645199, 236887691, this, false);
	}
	
	@Override
	public boolean equals( Object obj )
	{
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}

	@Override
	public KeyState clone()
	{
		try
		{
			return (KeyState) super.clone();
		}
		catch( CloneNotSupportedException e )
		{
			throw new InternalError();
		}
	}
	
	@Override
	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		builder.append( "keys", keys );
		builder.append( "upDown", updown );
		builder.append( "leftRight", leftright );
		builder.append( "actionPressed", isActionPressed() ? 1 : 0 );
		builder.append( "crouchPressed", isCrouchPressed() ? 1 : 0 );
		builder.append( "firePressed", isFirePressed() ? 1 : 0 );
		builder.append( "sprintPressed", isSprintPressed() ? 1 : 0 );
		builder.append( "secondAttackPressed", isSecondAttackPressed() ? 1 : 0 );
		builder.append( "jumpPressed", isJumpPressed() ? 1 : 0 );
		builder.append( "lookRightPressed", isLookRightPressed() ? 1 : 0 );
		builder.append( "handBreakPressed", isHandBreakPressed() ? 1 : 0 );
		builder.append( "lookLeftPressed", isLookLeftPressed() ? 1 : 0 );
		builder.append( "subMissionPressed", isSubMissionPressed() ? 1 : 0 );
		builder.append( "lookBehindPressed", isLookBehindPressed() ? 1 : 0 );
		builder.append( "walkPressed", isWalkPressed() ? 1 : 0 );
		builder.append( "analogUpPressed", isAnalogUpPressed() ? 1 : 0 );
		builder.append( "analogDownPressed", isAnalogDownPressed() ? 1 : 0 );
		builder.append( "analogLeftPressed", isAnalogLeftPressed() ? 1 : 0 );
		builder.append( "analogRightPressed", isAnalogRightPressed() ? 1 : 0 );
	    
		return builder.build();
	}
}
