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

package net.gtaun.shoebill.object;

import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.data.type.PlayerAttachBone;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author JoJLlmAn, MK124
 *
 */


public class PlayerAttach implements IPlayerAttach
{
	public class PlayerAttachSlot implements Slot
	{
		private int slot;
		
		private PlayerAttachBone bone = PlayerAttachBone.NOT_USABLE;
		private int modelId = 0;
		private Vector3D offset, rotate, scale;
		
		@Override public PlayerAttachBone getBone()			{ return bone; }
		@Override public int getModelId()					{ return modelId; }
		@Override public Vector3D getOffset()				{ return offset; }
		@Override public Vector3D getRotate()				{ return rotate; }
		@Override public Vector3D getScale()				{ return scale; }
		
		
		PlayerAttachSlot( int slot )
		{
			this.slot = slot;
		}

		@Override
		public boolean set( PlayerAttachBone bone, int modelId, Vector3D offset, Vector3D rot, Vector3D scale )
		{
			if( player.isOnline() == false ) return false;
			
			if( bone == PlayerAttachBone.NOT_USABLE ) return false;
			if( ! SampNativeFunction.setPlayerAttachedObject(player.getId(), slot, modelId, bone.getData(), offset.getX(), offset.getY(), offset.getZ(), rot.getX(), rot.getY(), rot.getZ(), scale.getX(), scale.getY(), scale.getZ()) ) 
				return false;
			
			this.bone = bone;
			this.modelId = modelId;
			
			this.offset = new Vector3D(offset);
			this.rotate = new Vector3D(rot);
			this.scale = new Vector3D(scale);
			
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

	
	private IPlayer player;
	private PlayerAttachSlot[] slots = new PlayerAttachSlot[MAX_ATTACHED_OBJECTS];

	
	PlayerAttach( IPlayer player )
	{
		this.player = player;
		
		for( int i=0; i<MAX_ATTACHED_OBJECTS; i++ )
		{
			slots[i] = new PlayerAttachSlot(i);
		}
	}
	
	@Override
	public IPlayer getPlayer()
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
