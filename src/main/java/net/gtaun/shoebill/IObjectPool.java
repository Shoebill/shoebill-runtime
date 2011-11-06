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

package net.gtaun.shoebill;

import java.util.Collection;

import net.gtaun.shoebill.object.IDialog;
import net.gtaun.shoebill.object.ILabel;
import net.gtaun.shoebill.object.IMenu;
import net.gtaun.shoebill.object.IObject;
import net.gtaun.shoebill.object.IPickup;
import net.gtaun.shoebill.object.IPlayer;
import net.gtaun.shoebill.object.IPlayerLabel;
import net.gtaun.shoebill.object.IPlayerObject;
import net.gtaun.shoebill.object.ITextdraw;
import net.gtaun.shoebill.object.ITimer;
import net.gtaun.shoebill.object.IVehicle;
import net.gtaun.shoebill.object.IZone;

/**
 * @author MK124
 *
 */

public interface IObjectPool
{
	IPlayer						getPlayer( int id );
	IVehicle					getVehicle( int id );
	IObject						getObject( int id );
	IPlayerObject				getPlayerObject( IPlayer player, int id );
	IPickup						getPickup( int id );
	ILabel						getLabel( int id );
	IPlayerLabel				getPlayerLabel( IPlayer player, int id );
	ITextdraw					getTextdraw( int id );
	IZone						getZone( int id );
	IMenu						getMenu( int id );
	
	Collection<IPlayer>			getPlayers();
	Collection<IVehicle>		getVehicles();
	Collection<IObject>			getObjects();
	Collection<IPlayerObject>	getPlayerObjects( IPlayer player );
	Collection<IPickup>			getPickups();
	Collection<ILabel>			getLabels();
	Collection<IPlayerLabel>	getpPlayerLabels( IPlayer player );
	Collection<ITextdraw>		getTextdraws();
	Collection<IZone>			getZones();
	Collection<IMenu>			getMenus();
	
	Collection<ITimer>			getTimers();
	Collection<IDialog>			getDialogs();
}
