package net.gtaun.shoebill.object.impl;

import java.util.LinkedList;
import java.util.Queue;

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.constant.MapIconStyle;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerMapIcon;

public class PlayerMapIconImpl implements PlayerMapIcon
{
	private Player player;
	private Queue<Integer> emptyIds;
	private int idCount;
	
	
	public PlayerMapIconImpl(Player player)
	{
		this.player = player;
		emptyIds = new LinkedList<>();
		idCount = 0;
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public MapIcon createIcon(float x, float y, float z, int markerType, Color color, MapIconStyle style)
	{
		MapIcon icon = createIcon();
		icon.update(x, y, z, markerType, color, style);
		return icon;
	}
	
	@Override
	public MapIcon createIcon(Vector3D pos, int markerType, Color color, MapIconStyle style)
	{
		MapIcon icon = createIcon();
		icon.update(pos, markerType, color, style);
		return icon;
	}
	
	@Override
	public MapIcon createIcon()
	{
		final int iconId;
		if (!emptyIds.isEmpty()) iconId = emptyIds.poll();
		else
		{
			iconId = idCount;
			idCount++;
		}
		
		return new MapIcon()
		{
			private int id = iconId;
			
			@Override
			protected void finalize() throws Throwable
			{
				destroy();
				super.finalize();
			}
			
			@Override
			public int getId()
			{
				return id;
			}
			
			@Override
			public Player getPlayer()
			{
				return player;
			}
			
			@Override
			public boolean isDestroyed()
			{
				return id != -1;
			}
			
			@Override
			public void destroy()
			{
				if (isDestroyed()) return;
				
				SampNativeFunction.removePlayerMapIcon(player.getId(), id);
				emptyIds.offer(id);
				id = -1;
			}
			
			@Override
			public void update(Vector3D pos, int markerType, Color color, MapIconStyle style)
			{
				update(pos.getX(), pos.getY(), pos.getZ(), markerType, color, style);
			}
			
			@Override
			public void update(float x, float y, float z, int markerType, Color color, MapIconStyle style)
			{
				SampNativeFunction.setPlayerMapIcon(player.getId(), id, x, y, z, markerType, color.getValue(), style.getValue());
			}
		};
	}
}
