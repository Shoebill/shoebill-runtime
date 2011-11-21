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

import net.gtaun.shoebill.object.Dialog;
import net.gtaun.shoebill.object.Label;
import net.gtaun.shoebill.object.Menu;
import net.gtaun.shoebill.object.ObjectBase;
import net.gtaun.shoebill.object.Pickup;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerLabel;
import net.gtaun.shoebill.object.PlayerObject;
import net.gtaun.shoebill.object.Textdraw;
import net.gtaun.shoebill.object.Timer;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.shoebill.object.Zone;

/**
 * @author MK124
 *
 */

public interface ISampObjectPool
{
	public Player getPlayer( int id );
	public Vehicle getVehicle( int id );
	public ObjectBase getObject( int id );
	public PlayerObject getPlayerObject( Player player, int id );
	public Pickup getPickup( int id );
	public Label getLabel( int id );
	public PlayerLabel getPlayerLabel( Player player, int id );
	public Textdraw getTextdraw( int id );
	public Zone getZone( int id );
	public Menu getMenu( int id );
	public Dialog getDialog( int id );
	
	public Collection<Player> getPlayers();
	public Collection<Vehicle> getVehicles();
	public Collection<ObjectBase> getObjects();
	public Collection<PlayerObject> getPlayerObjects( Player player );
	public Collection<Pickup> getPickups();
	public Collection<Label> getLabels();
	public Collection<PlayerLabel> getPlayerLabels( Player player );
	public Collection<Textdraw> getTextdraws();
	public Collection<Zone> getZones();
	public Collection<Menu> getMenus();
	public Collection<Dialog> getDialogs();
	public Collection<Timer> getTimers();

	public <T extends Player> Collection<T> getPlayers( Class<T> cls );
	public <T extends Vehicle> Collection<T> getVehicles( Class<T> cls );
	public <T extends ObjectBase> Collection<T> getObjects( Class<T> cls );
	public <T extends PlayerObject> Collection<T> getPlayerObjects( Player player, Class<T> cls );
	public <T extends Pickup> Collection<T> getPickups( Class<T> cls );
	public <T extends Label> Collection<T> getLabels( Class<T> cls );
	public <T extends PlayerLabel> Collection<T> getPlayerLabels( Player player, Class<T> cls );
	public <T extends Textdraw> Collection<T> getTextdraws( Class<T> cls );
	public <T extends Zone> Collection<T> getZones( Class<T> cls );
	public <T extends Menu> Collection<T> getMenus( Class<T> cls );
	public <T extends Dialog> Collection<T> getDialogs( Class<T> cls );
	public <T extends Timer> Collection<T> getTimers( Class<T> cls );
}
