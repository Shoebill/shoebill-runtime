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

package net.gtaun.shoebill.about;

import java.awt.Desktop;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.net.URI;

/**
 * @author MK124
 *
 */

public class MainFrame extends Frame
{
	private static final long serialVersionUID = 1L;
	private Label caption = null;
	private Label description = null;
	private Label version = null;


	public MainFrame()
	{
		super();
		initialize();
	}


	private void initialize()
	{
		version = new Label();
		version.setBounds(new Rectangle(106, 86, 228, 18));
		version.setText("Milestone 1 (20110701)");
		version.setFont(new Font("Dialog", Font.BOLD, 12));
		version.setAlignment(Label.CENTER);
		description = new Label();
		description.setText("Java for SA:MP DEVKIT");
		description.setAlignment(Label.CENTER);
		description.setBounds(new Rectangle(106, 67, 228, 13));
		caption = new Label();
		caption.setFont(new Font("Dialog", Font.PLAIN, 24));
		caption.setAlignment(Label.CENTER);
		caption.setText("Project Shoebill");
		caption.setBounds(new Rectangle(78, 39, 284, 36));
		caption.setName("caption");
		this.setLayout(null);
		this.setSize(441, 124);
		this.setName("about");
		this.setResizable(false);
		this.setTitle("About");

		this.add(description, null);
		this.add(caption, null);
		this.add(version, null);
		
		this.setLocationRelativeTo( null );

		this.addMouseListener( mouseAdapter );
		caption.addMouseListener( mouseAdapter );
		description.addMouseListener( mouseAdapter );
		version.addMouseListener( mouseAdapter );
	}
	
	
	MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter()
	{
		public void mouseClicked( java.awt.event.MouseEvent e )
		{
			URI uri;
			try
			{
				uri = new java.net.URI("https://github.com/mk124/Shoebill");
				Desktop.getDesktop().browse(uri);
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
		}
	};
}
