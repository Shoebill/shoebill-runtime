/**
 * Copyright (C) 2011-2012 MK124
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

import net.gtaun.shoebill.ShoebillImpl;
import net.gtaun.shoebill.constant.VehicleModelInfoType;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.Server;
import net.gtaun.shoebill.samp.SampNativeFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public abstract class ServerImpl implements Server
{
	public ServerImpl()
	{

	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public int getServerCodepage()
	{
		return SampNativeFunction.getServerCodepage();
	}
	
	@Override
	public void setServerCodepage(int codepage)
	{
		SampNativeFunction.setServerCodepage(codepage);
	}
	
	@Override
	public int getMaxPlayers()
	{
		return SampNativeFunction.getMaxPlayers();
	}
	
	@Override
	public void setGamemodeText(String text)
	{
		SampNativeFunction.setGameModeText(text);
	}
	
	@Override
	public void sendRconCommand(String command)
	{
		if (command == null) throw new NullPointerException();
		SampNativeFunction.sendRconCommand(command);
	}
	
	@Override
	public void connectNPC(String name, String script)
	{
		if (name == null || script == null) throw new NullPointerException();
		SampNativeFunction.connectNPC(name, script);
	}
	
	@Override
	public String getServerVarAsString(String varname)
	{
		if (varname == null) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsString(varname);
	}
	
	@Override
	public int getServerVarAsInt(String varname)
	{
		if (varname == null) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsInt(varname);
	}
	
	@Override
	public boolean getServerVarAsBool(String varname)
	{
		if (varname == null) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsBool(varname);
	}

	@Override
	public void sendMessageToAll(Color color, String message)
	{
		for (Player player : ShoebillImpl.getInstance().getSampObjectStore().getPlayers())
		{
			player.sendMessage(color, message);
		}
	}

	@Override
	public void sendMessageToAll(Color color, String format, Object... args)
	{
		for (Player player : ShoebillImpl.getInstance().getSampObjectStore().getPlayers())
		{
			String message = String.format(format, args);
			player.sendMessage(color, message);
		}
	}

	@Override
	public void gameTextToAll(int time, int style, String text)
	{
		SampNativeFunction.gameTextForAll(text, time, style);
	}

	@Override
	public void gameTextToAll(int time, int style, String format, Object... args)
	{
		String text = String.format(format, args);
		SampNativeFunction.gameTextForAll(text, time, style);
	}
	
	@Override
	public Vector3D getVehicleModelInfo(int modelId, VehicleModelInfoType infotype)
	{
		Vector3D vector = new Vector3D();
		SampNativeFunction.getVehicleModelInfo(modelId, infotype.getValue(), vector);
		return vector;
	}
}
