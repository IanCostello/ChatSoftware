package chatC;

import java.awt.Rectangle;

public class CollisionDetector {
	public static boolean collidesWith(int mx, int my, Rectangle r) {
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
