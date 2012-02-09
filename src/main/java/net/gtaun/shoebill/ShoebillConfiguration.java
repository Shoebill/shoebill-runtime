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

package net.gtaun.shoebill;

import java.io.File;
import java.io.InputStream;
import net.gtaun.shoebill.util.config.YamlConfiguration;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class ShoebillConfiguration
{
	private File workdir;
	private String gamemode;
	
	
	public ShoebillConfiguration( InputStream in )
	{
		YamlConfiguration config = new YamlConfiguration();
		config.read( in );

		String workdirPath = config.getString( "workdir", "./shoebill/" );
		workdir = new File( workdirPath );
		
		gamemode = config.getString("gamemode");
	}
	
	public File getWorkdir()
	{
		return workdir;
	}

	public String getGamemode()
	{
		return gamemode;
	}
}