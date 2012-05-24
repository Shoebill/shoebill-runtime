/**
 * Copyright (C) 2012 MK124
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

package net.gtaun.shoebill.trait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MK124
 *
 */

public class TraitManager implements ITraitManager
{
	private Map<Class<? extends Trait>, Trait> traits = new HashMap<>();
	
	
	public TraitManager()
	{
		
	}

	@Override
	public <T extends Trait> void registerTrait( Class<T> traitType, T trait ) throws IllegalArgumentException, UnsupportedOperationException
	{
		if( traitType == Trait.class || traitType == StaticTrait.class || traitType.isInstance(trait) == false )
		{
			throw new IllegalArgumentException();
		}
		if( StaticTrait.class.isAssignableFrom(traitType) && traits.containsKey(traitType) )
		{
			throw new UnsupportedOperationException();
		}
		
		List<? extends Number> list = new ArrayList<Number>();
		list.add( null );

		traits.put( traitType, trait );
	}

	@Override
	public <T extends Trait> void removeTrait( Class<T> traitType ) throws UnsupportedOperationException
	{
		if( StaticTrait.class.isAssignableFrom(traitType) )
		{
			throw new UnsupportedOperationException();
		}
		
		traits.remove( traitType );
	}

	@Override
	public <T extends Trait> boolean checkTrait( Class<T> traitType )
	{
		return traits.containsKey( traitType );
	}

	@Override
	public <T extends Trait> T getTrait( Class<T> traitType )
	{
		return traitType.cast( traits.get(traitType) );
	}
}
