/**
 * Copyright (C) 2011 JoJLlmAn
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

import net.gtaun.shoebill.data.Point3D;
import net.gtaun.shoebill.data.constant.PlayerAttachBone;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerAttach;
import net.gtaun.shoebill.samp.SampNativeFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author JoJLlmAn, MK124
 *
 */


public class PlayerAttachImpl implements PlayerAttach
{
	public class PlayerAttachSlot implements Slot
	{
		private int slot;
		
		private PlayerAttachBone bone = PlayerAttachBone.NOT_USABLE;
		private int modelId = 0;
		private Point3D offset, rotate, scale;
		
		
		PlayerAttachSlot( int slot )
		{
			this.slot = slot;
		}

		@Override
		public String toString()
		{
			return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
		}
		
		@Override public PlayerAttachBone getBone()
		{
			return bone;
		}
		
		@Override public int getModelId()
		{
			return modelId;
		}
		@Override public Point3D getOffset()
		{
			return offset.clone();
		}
		@Override public Point3D getRotate()
		{
			return rotate.clone();
		}
		@Override public Point3D getScale()
		{
			return scale.clone();
		}

		@Override
		public boolean set( PlayerAttachBone bone, int modelId, Point3D offset, Point3D rot, Point3D scale )
		{
			if( player.isOnline() == false ) return false;
			
			if( bone == PlayerAttachBone.NOT_USABLE ) return false;
			if( ! SampNativeFunction.setPlayerAttachedObject(player.getId(), slot, modelId, bone.getData(), offset.getX(), offset.getY(), offset.getZ(), rot.getX(), rot.getY(), rot.getZ(), scale.getX(), scale.getY(), scale.getZ()) )
			{
				return false;
			}
			
			this.bone = bone;
			this.modelId = modelId;
			
			this.offset = new Point3D(offset);
			this.rotate = new Point3D(rot);
			this.scale = new Point3D(scale);
			
			return true;
		}

		@Override
		public boolean remove()
		{
			if( player.isOnline() == false ) return false;
			
			if( bone == PlayerAttachBone.NOT_USABLE ) return false;
			if( ! SampNativeFunction.removePlayerAttachedObject(player.getId(), slot) ) return false;
			
			bone = PlayerAttachBone.NOT_USABLE;
			modelId = 0;
			
			offset = null;
			rotate = null;
			scale = null;
			
			return true;
		}

		@Override
		public boolean isUsed( int slot )
		{
			if( player.isOnline() == false ) return false;
			
			return bone != PlayerAttachBone.NOT_USABLE;
		}
	}

	
	private Player player;
	private PlayerAttachSlot[] slots = new PlayerAttachSlot[MAX_ATTACHED_OBJECTS];

	
	PlayerAttachImpl( Player player )
	{
		this.player = player;
		
		for( int i=0; i<MAX_ATTACHED_OBJECTS; i++ )
		{
			slots[i] = new PlayerAttachSlot(i);
		}
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public Slot getSlot( int slot )
	{
		return slots[ slot ];
	}

	@Override
	public Slot[] getSlots()
	{
		return slots.clone();
	}
}
