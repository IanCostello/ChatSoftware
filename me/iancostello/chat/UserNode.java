package me.iancostello.chat;

import me.iancostello.util.ByteBuffer;

/** UserNode
 * Nodes for the server
 */
public class UserNode {
	private int level2;
	private String nodeName2;
	private String information2;
	boolean top2;

	/** Constuctor
	 * Used for Random Generic Information
	 * @param s
	 */
	public UserNode(String s) {
		information2 = s;
		level2 = -1;
	}

	public UserNode(int level, String nodeName, String information) {
		level2 = level;
		nodeName2 = nodeName;
		information2 = information;
	}

	public UserNode(String nodeName, int level, boolean top) {
		nodeName2 = nodeName;
		top2 = top;
		level2 = level;
		information2 = null;
	}

	/** getters and setters */
	public String toString() {
		return this.getFormattedNode().toString();
	}

	public ByteBuffer getFormattedNode() {
		ByteBuffer bBuf = new ByteBuffer();
		if (information2 == null) {
			for (int i = 0; i < level2; i+=1) {
				bBuf.append('\t');
			}
			if (top2) {
				bBuf.append('<');
				bBuf.append(nodeName2);
				bBuf.append('>');
			} else {
				bBuf.append('<');
				bBuf.append('/');
				bBuf.append(nodeName2);
				bBuf.append('>');

			}			
		} else if (level2 == -1) {
			return new ByteBuffer(information2);
		} else {
			for (int i = 0; i < level2; i+=1) {
				bBuf.append('\t');
			}
			bBuf.append('<');
			bBuf.append(nodeName2);
			bBuf.append('>');
			bBuf.append(information2);
			bBuf.append('<');
			bBuf.append('/');
			bBuf.append(nodeName2);
			bBuf.append('>');
		}
		bBuf.append('\n');
		return bBuf;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level2;
	}

	/**
	 * @param level2 the level2 to set
	 */
	public void setLevel(int level2) {
		this.level2 = level2;
	}

	/**
	 * @return the nodeName2
	 */
	public String getNodeName() {
		return nodeName2;
	}

	/**
	 * @param nodeName2 the nodeName2 to set
	 */
	public void setNodeName(String nodeName2) {
		this.nodeName2 = nodeName2;
	}

	/**
	 * @return the information2
	 */
	public String getInformation() {
		return information2;
	}

	/**
	 * @param information2 the information2 to set
	 */
	public void setInformation(String information2) {
		this.information2 = information2;
	}

	/**
	 * @return the top2
	 */
	public boolean isTop() {
		return top2;
	}

	/**
	 * @param top2 the top2 to set
	 */
	public void setTop(boolean top2) {
		this.top2 = top2;
	}

}
