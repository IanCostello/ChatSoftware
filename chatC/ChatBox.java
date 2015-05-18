package chatC;

import java.awt.Rectangle;

public class ChatBox {
	private Rectangle box;
	private boolean focused;
	private boolean protectedInfo;
	private boolean visible;
	private String content = "";
	private int wrap;
	private int sizeOfChar;
	private boolean shouldWrite;
	private int maxLength;
	
	/** Constuctor
	 * @param rect
	 */
	public ChatBox(Rectangle rect, boolean secret, int charSize, int maxLength) {
		box = rect;
		protectedInfo = secret;
		wrap = (int)(box.width*1.85)/charSize;
		sizeOfChar = charSize;
		this.maxLength = maxLength;
	}
	
	public void setShouldWrite(boolean b) {
		shouldWrite = b;
	}

	public int getLength() {
		return content.length();
	}
	
	public boolean shouldWrite() {
		return shouldWrite;
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	public void setProtected(boolean b) {
		protectedInfo = b;
	}
	
	public String getContent() {
		return content;
	}
	
	public String toString() {
		if (protectedInfo) {
			String s = "";
			for (int i = 0; i < content.length(); i+=1) {
				s += '*';
			}
			return s;
		} else {
			String s = "";
			for (int i = 0; i < content.length(); i+=1) {
				s += content.charAt(i);
			}	
			return s;
		}
	}

	public void setRect(Rectangle rect) {
		box = rect;
	}
	
	public void clear() {
		content = "";
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public Rectangle getRect() {
		return box;
	}

	public void setVisible(boolean state) {
		visible = state;
	}

	/** appendChar
	 * 
	 */
	public void appendChar(char c) {
		int length;
		if (maxLength == -1) {
			content +=c;
		} else {
			if (content.length() < maxLength) {
				content += c;
			}
		}
	}

	public void setBoxX(int newX) {
		box.x = newX;
	}
	
	public void setCharSize(int i) {
		sizeOfChar = i;
	}
	
	public void deleteChar() {
		if (content.length()!=0) {
			content = content.substring(0, content.length() - 1);
		}
	}

	public void setFocused(boolean state) {
		focused = state;
	}

	public boolean isFocused() {
		return focused;
	}

	public int getX() {
		return box.x;
	}

	public int getY() {
		return box.y;
	}

	public int getHeight() {
		return box.height;
	}

	public int getWidth() {
		return box.width;
	}
}
