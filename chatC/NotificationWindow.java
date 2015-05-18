package chatC;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class NotificationWindow {

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
			int width = 200;
			int height = 50;
			JFrame frame = new JFrame("Notification Window");
			frame.getContentPane().add(new JLabel(message));
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
