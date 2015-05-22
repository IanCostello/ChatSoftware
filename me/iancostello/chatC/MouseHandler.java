package me.iancostello.chatC;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** MouseHandler
 * Basic Mouse Handler
 * @author iancostello
 */
public class MouseHandler implements MouseListener {
	int x;
	int y;
	CollisionDetector cd = new CollisionDetector();
	DataInterface data;

	public MouseHandler(DataInterface data) {
		super();
		this.data = data;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		System.out.println("X:" + x + " Y:" + y);		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		//If the first input box is pressed
		if (data.getpLogin().isVisible() && cd.collidesWith(x, y, data.getuLoginR())) {
			data.getpLogin().setFocused(false);
			data.getuLogin().setFocused(true);
			data.getChatBox().setFocused(false);
			//If the second input box is pressed
		} else if (data.getuLogin().isVisible() && cd.collidesWith(x, y, data.getpLoginR())) {
			data.getpLogin().setFocused(true);
			data.getuLogin().setFocused(false);
			data.getChatBox().setFocused(false);
			//Do nothing else if its in the login screen
		} else if (data.isInLoginScreen()) {
			//If the Close Button is pressed 
		} else if (data.expandBoxPressed() && cd.collidesWith(x, y, data.getExpandBox())) {
			data.setExpandBoxPressed(false);
			data.setExpandBoxChanged(true);
			data.setContactsPressed(false);
			data.setContactsPressed(false);
			data.getpLogin().setFocused(false);
			data.getuLogin().setFocused(false);
			data.getChatBox().setFocused(true);
			data.setBigChange(true);
			//If the contact button is pressed
		} else if (cd.collidesWith(x, y, data.getContacts())) {
			if (data.isContactsPressed()) {
				data.setContactsPressed(false);
				data.setExpandBoxPressed(false);
			} else {
				data.setContactsPressed(true);
				data.setExpandBoxPressed(true);
			}
			data.setConnectMenu(false);
			//If the chat box is pressed
		} else if ((data.getChatBox().isVisible()) && (cd.collidesWith(x, y, data.getChatBox().getRect()))) {
			data.getChatBox().setFocused(true);
			//If the send button is pressed
		} else if (!data.isExpandBoxPressed() && cd.collidesWith(x, y, data.getSendButton())) {
			if (data.getChatBox().isFocused()) {
				data.getChatBox().setShouldWrite(true);
			}
			System.out.println("sent");
			//If someone clicks on the lock
		} else if (cd.collidesWith(x, y, data.getLockRectangle())) {
			System.out.println("Lock");
			//Get the location of the contacts pressed
		} else if (x > 630 && data.expandBoxPressed()) {
			data.setContactsPressed(false);
			data.setConnectMenu(false);
			data.setExpandBoxPressed(false);
		} else if (data.isContactsPressed() && data.isExpandBoxPressed() && !(data.isConnectMenu())) {
			if ((x>30) && (x<555) && (y>100) && (y < 400)) {
				int column = (x - 30)/175;
				int row = (y-100) / 50;
				data.setCurrentUser((row * 3) + column);
				data.setExpandBoxPressed(false);
				data.setBigChange(true);
			}
		}  else {
			data.getChatBox().setFocused(false);
		}
		data.setSomethingChanged(true);
		data.setBigChange(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
