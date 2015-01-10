/**
 * Copyright (C) 2011 JoJLlmAn
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

import net.gtaun.shoebill.SampEventDispatcher;
import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.constant.PlayerAttachBone;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerAttach;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author JoJLlmAn, MK124 & 123marvin123
 */
public class PlayerAttachImpl implements PlayerAttach
{
	public class PlayerAttachSlotImpl implements PlayerAttachSlot
	{
		private int slot;
		
		private PlayerAttachBone bone = PlayerAttachBone.NOT_USABLE;
		private int modelId = 0;
		private Vector3D offset, rotate, scale;
		
		
		PlayerAttachSlotImpl(int slot)
		{
			this.slot = slot;
		}
		
		@Override
		public String toString()
		{
			return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("player", player).append("slot", slot).append("bone", bone)
				.append("modelId", modelId).toString();
		}
		
		@Override
		public Player getPlayer()
		{
			return player;
		}

		@Override
		public int getSlot()
		{
			return slot;
		}
		
		@Override
		public PlayerAttachBone getBone()
		{
			return bone;
		}
		
		@Override
		public int getModelId()
		{
			return modelId;
		}
		
		@Override
		public Vector3D getOffset()
		{
			return offset.clone();
		}
		
		@Override
		public Vector3D getRotation()
		{
			return rotate.clone();
		}
		
		@Override
		public Vector3D getScale()
		{
			return scale.clone();
		}
		
		@Override
		public boolean set(PlayerAttachBone bone, int modelId, Vector3D offset, Vector3D rot, Vector3D scale, int materialcolor1, int materialcolor2)
		{
			if (player.isOnline() == false) return false;
			
			if (bone == PlayerAttachBone.NOT_USABLE) return false;
			final boolean[] cont = {true};
			SampEventDispatcher.getInstance().executeWithoutEvent(() -> {
				if (!SampNativeFunction.setPlayerAttachedObject(player.getId(), slot, modelId, bone.getValue(), offset.getX(), offset.getY(), offset.getZ(), rot.getX(), rot.getY(), rot.getZ(), scale.getX(), scale.getY(), scale.getZ(), materialcolor1, materialcolor2))
				{
					cont[0] = false;
				}
			});
			if(!cont[0]) return false;
			this.bone = bone;
			this.modelId = modelId;
			
			this.offset = new Vector3D(offset);
			this.rotate = new Vector3D(rot);
			this.scale = new Vector3D(scale);
			
			return true;
		}

		public boolean setWithoutExec(PlayerAttachBone bone, int modelId, Vector3D offset, Vector3D rot, Vector3D scale, int materialcolor1, int materialcolor2)
		{
			if (!player.isOnline()) return false;

			if (bone == PlayerAttachBone.NOT_USABLE) return false;

			this.bone = bone;
			this.modelId = modelId;

			this.offset = new Vector3D(offset);
			this.rotate = new Vector3D(rot);
			this.scale = new Vector3D(scale);

			return true;
		}

		@Override
		public boolean remove() {
			final boolean[] success = {false};
			SampEventDispatcher.getInstance().executeWithoutEvent(() -> success[0] = SampNativeFunction.removePlayerAttachedObject(player.getId(), slot) && removeWithoutExec());
			return success[0];
		}

		public boolean removeWithoutExec()
		{
			if (player.isOnline() == false) return false;

			if (bone == PlayerAttachBone.NOT_USABLE) return false;

			bone = PlayerAttachBone.NOT_USABLE;
			modelId = 0;

			offset = null;
			rotate = null;
			scale = null;

			return true;
		}

		@Override
		public boolean isUsed()
		{
			if (player.isOnline() == false) return false;
			
			return bone != PlayerAttachBone.NOT_USABLE;
		}
		
		@Override
		public boolean edit()
		{
			if (player.isOnline() == false) return false;
			
			return SampNativeFunction.editAttachedObject(player.getId(), slot);
		}
	}
	
	
	private Player player;
	private PlayerAttachSlot[] slots = new PlayerAttachSlot[MAX_ATTACHED_OBJECTS];
	
	
	PlayerAttachImpl(Player player)
	{
		this.player = player;
		
		for (int i = 0; i < MAX_ATTACHED_OBJECTS; i++)
		{
			slots[i] = new PlayerAttachSlotImpl(i);
		}
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("player", player).toString();
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public PlayerAttachSlot getSlot(int slot)
	{
		return slots[slot];
	}
	
	@Override
	public PlayerAttachSlot getSlotByBone(PlayerAttachBone bone)
	{
		for (PlayerAttachSlot slot : slots)
		{
			if (slot == null) continue;
			if (slot.getBone() == bone) return slot;
		}
			
		return null;
	}
	
	@Override
	public PlayerAttachSlot[] getSlots()
	{
		return slots.clone();
	}
}
