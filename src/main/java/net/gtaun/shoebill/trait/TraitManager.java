package net.gtaun.shoebill.trait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
