/** NotificationWindow 
 * Creates a Notification New Window
 * 
 */
package chatC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;

/** Notification Window
 * Interface for creating notifications
 */
public class NotificationWindow {
	Color topAreaBlue = new Color(131, 118, 162);
	Color mainAreaWhite = new Color(222, 222, 222);
	Color notificationBlack = new Color(120, 120, 120);
	
	public void createWindow(String s) {
		Window window = new Window(s);
		Thread t = new Thread(window);
		t.start();
	}
	
	public class Window implements Runnable {
		String message;
		
		public Window(String message) {
			this.message = message;
		}
		
		public void run() {
			//Width of Window
			int width = 200;
			int height = 50;
			JFrame frame = new JFrame("Notification Window");
			int fontSize = 14;
			//Create a new window in the top right of the screen that is undecorated
			frame.setUndecorated(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(width, height);
			frame.setAlwaysOnTop(true);
			frame.setAutoRequestFocus(false);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double screenWidth = screenSize.getWidth();
			frame.setLocation((int)screenWidth-width-5, 30);
			frame.setFocusable(false);
			frame.setVisible(true);
			Graphics g = frame.getGraphics();
			//TODO Change this value up
			try {
				Thread.sleep(75);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			//Looks of the window
			g.setColor(topAreaBlue);
			g.fillRect(0, 0, width, 20);
			g.setColor(mainAreaWhite);
			g.fillRect(0, 20, width, height - 20);
			g.setFont(new Font(Font.SERIF, 12, 12));
			g.drawString("Secure Chat Notification", 40, 14);
			g.setFont(new Font(Font.SANS_SERIF, 14, 14));
			FontMetrics fontMet = g.getFontMetrics();
			int widthOfMessage = fontMet.stringWidth(message);
			while (widthOfMessage > (width)) {
				fontSize -=1;
				fontMet = g.getFontMetrics();
				widthOfMessage = fontMet.stringWidth(message);
				g.setFont(new Font(Font.SANS_SERIF, fontSize, fontSize));
			}
			g.setColor(notificationBlack);
			g.drawString(message, width/2 - widthOfMessage/2, 40);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frame.dispose();
		}	
	}
}
