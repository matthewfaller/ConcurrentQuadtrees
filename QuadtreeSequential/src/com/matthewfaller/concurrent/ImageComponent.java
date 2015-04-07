package com.matthewfaller.concurrent;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

public class ImageComponent extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image content;
	
	public ImageComponent(Image img){
		this.content = img;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		if(content != null)
			g.drawImage(content, 0, 0, null);
	}
}
