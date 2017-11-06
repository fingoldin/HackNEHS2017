package com.stickfigure;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.*;
import com.sun.jna.Native;
import com.sun.jna.platform.*;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef.HWND;

public class Renderer extends JPanel {
	
	static boolean punch;
	static Image[] punchLFrames = new Image[39];
	static Image[] punchRFrames = new Image[39];
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 100, 100);
	}
	
	public Renderer() throws IOException {
	    Window w = new Window(null);

	    //setting window boundaries
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    w.setBounds(0, 0, screenSize.width, screenSize.height - 50);
	    
	    //window init
	    w.setVisible(true);
	    w.setAlwaysOnTop(true);
	    
	    //window visual transparency
	    w.setOpacity(0.5f);
	    
	    //window event transparency
	    setTransparent(w);
	    
	    //loading punch images
	    for (int i = 0; i < 39; i++) {
	    	punchLFrames[i] = new ImageIcon("C:\\Users\\sharissa\\workspace5\\hackNEHS_2017\\punch0" + ((Integer) i).toString() + ".png").getImage();
	    	punchRFrames[i] = new ImageIcon("C:\\Users\\sharissa\\workspace5\\hackNEHS_2017\\punch0" + ((Integer) i).toString() + ".png").getImage();
	    	
	    	BufferedImage bi = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
	    	Graphics2D g = bi.createGraphics();
	    }
	}
	
	//animID = the animation used (punching, running, jumping)
	//animPos = int between 0 and 1 to find the animation frame
	void drawSprite (float x, float y, int animID, float aP) {
		if (animID == 2) { //punch left
			int animPos = ((int) aP * 39) - 1;
			punchLFrames[animPos].getGraphics().drawImage(punchLFrames[animPos], 400, 400, null);
		}
		
		if (animID == 3) { //punch right
			int animPos = ((int) aP * 39) - 1;
			punchRFrames[animPos].getGraphics().drawImage(punchRFrames[animPos], 400, 400, null);
		}
	}

	private void setTransparent(Component w) {
	    WinDef.HWND hwnd = getHWnd(w);
	    int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
	    wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
	    User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
	}


	private HWND getHWnd(Component w) {
	    HWND hwnd = new HWND();
	    hwnd.setPointer(Native.getComponentPointer(w));
	    return hwnd;
	}
	
}
