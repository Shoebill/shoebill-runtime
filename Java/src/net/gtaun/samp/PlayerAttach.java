/**
 * Copyright (C) 2011 JoJLlmAn
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

package net.gtaun.samp;

import net.gtaun.samp.data.Vector3D;

/**
 * @author JoJLlmAn
 *
 */

public class PlayerAttach {
	
	int playerid;
	
	int[] models = new int[5];
	int[] bones = new int[5];

	public PlayerAttach(int playerid) {
		
		this.playerid = playerid;
		
		for(int i=0;i<5;i++)
		{
			models[i] = -1;
			bones[i] = -1;
		}
	}
	
	public boolean set(int slot, int modelid, int bone, Vector3D offset, Vector3D rot, Vector3D scale)
	{
		boolean success = NativeFunction.setPlayerAttachedObject(playerid, slot, modelid, bone, offset.x, offset.y, offset.z, rot.x, rot.y, rot.z, scale.x, scale.y, scale.z);
		
		if(success)
		{
			models[slot] = modelid;
			bones[slot] = bone;
		}
		
		return success;
	}
	
	public boolean remove(int slot)
	{
		boolean success = NativeFunction.removePlayerAttachedObject(playerid, slot);
		
		if(success)
		{
			models[slot] = -1;
			bones[slot] = -1;
		}
		
		return success;
	}
	
	public boolean isSlotUsed(int slot)
	{
		return models[slot] != -1;
	}
	
	public int getModel(int slot)
	{
		return models[slot];
	}
	
	public int getBone(int slot)
	{
		return bones[slot];
	}
}
