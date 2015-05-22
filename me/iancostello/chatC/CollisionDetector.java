package me.iancostello.chatC;

import java.awt.Rectangle;

/** CollisionDetector
 * Returns whether or not an x and y cordinate collided with a Rectangle
 */
public class CollisionDetector {
	public boolean collidesWith(int mx, int my, Rectangle r) {
		try {
			if ((r.x <= mx) && (mx < r.x + r.width) && (r.y <= my) && (my < r.y + r.height)) {
				return true;
			}
		} catch (NullPointerException e) {
			return false;
		}
		
		return false;
	}
}
