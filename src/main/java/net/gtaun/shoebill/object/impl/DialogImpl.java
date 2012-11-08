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

import net.gtaun.shoebill.constant.DialogStyle;
import net.gtaun.shoebill.object.Dialog;
import net.gtaun.shoebill.object.Player;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public abstract class DialogImpl implements Dialog
{
	private static int count = 0;
	
	
	private int id;
	
	
	public DialogImpl()
	{
		initialize();
	}
	
	private void initialize()
	{
		id = count;
		count++;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public int getId()
	{
		return id;
	}
	
	@Override
	public void show(Player player, DialogStyle style, String caption, String text, String button1, String button2)
	{
		player.showDialog(this, style, caption, text, button1, button2);
	}
	
	@Override
	public void cancel(Player player)
	{
		if (player.getDialog() != this) return;
		player.cancelDialog();
	}
}
