package chatC;

import me.iancostello.util.ByteBuffer;

/** ClientUserNode
 * node ready for printing 
 */
public class ClientUserNode {
	private int level;
	private String nodeName;
	private String information;
	Boolean top;

	/** Constuctor
	 * Used for Random Generic Information
	 * @param s
	 */
	public ClientUserNode(String s) {
		this.information = s;
		this.level = -1;
	}

	public ClientUserNode(int level, String nodeName, String information) {
		this.level = level;
		this.nodeName = nodeName;
		this.information = information;
	}

	public ClientUserNode(String nodeName, int level, boolean top) {
		this.nodeName = nodeName;
		this.top = top;
		this.level = level;
		this.information = null;
	}

	public ClientUserNode(int level, String nodeName, byte[] buf) {
		this.level = level;
		this.nodeName = nodeName;
		ByteBuffer temp = new ByteBuffer();
		temp.append(buf);
		this.information = temp.toString();
	}
	
	/** getters and setters */
	public String toString() {
		return this.getFormattedNode().toString();
	}

	public ByteBuffer getFormattedNode() {
		ByteBuffer bBuf = new ByteBuffer();
		if (top != null) {
			for (int i = 0; i < level; i+=1) {
				bBuf.append('\t');
			}
			if (top) {
				bBuf.append('<');
				bBuf.append(nodeName);
				bBuf.append('>');
			} else {
				bBuf.append('<');
				bBuf.append('/');
				bBuf.append(nodeName);
				bBuf.append('>');

			}			
		} else if (level == -1) {
			return new ByteBuffer(information);
		} else {
			for (int i = 0; i < level; i+=1) {
				bBuf.append('\t');
			}
			bBuf.append('<');
			bBuf.append(nodeName);
			bBuf.append('>');
			if (information!=null) {
				bBuf.append(information);
			}
			bBuf.append('<');
			bBuf.append('/');
			bBuf.append(nodeName);
			bBuf.append('>');
		}
		bBuf.append('\n');
		return bBuf;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return the information
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @param information the information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}

	/**
	 * @return the top
	 */
	public boolean isTop() {
		return top;
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(boolean top) {
		this.top = top;
	}

}
