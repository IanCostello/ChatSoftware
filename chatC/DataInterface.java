package chatC;

import java.awt.Rectangle;
import java.util.ArrayList;

public class DataInterface {
	private boolean expandBoxPressed;
	private Rectangle expandBox;
	private Rectangle sendButtom;
	private Rectangle lockRectangle;
	private ChatBox chatBox;
	private ChatBox uLogin;
	private ChatBox pLogin;
	private boolean lockPressed;
	private boolean expandBoxChanged;
	private boolean somethingChanged;
	private Rectangle uLoginR;
	private Rectangle pLoginR;
	private boolean inLoginScreen = true;
	private boolean shouldLogin;
	private boolean bigChange;
	private int currentUser;
	private boolean contactsPressed;
	private Rectangle contacts;
	private int multiUserOldSize;
	private Rectangle sideBar;
	private boolean connectMenu;
	private boolean firstRunSinceLogin = true;
	private ClientXML xml;
	private ClientSocket socket;
	private long loginTime;
	private boolean setWaitChange;
	
	public DataInterface(int version) {
		xml = new ClientXML((System.getProperty("user.home") + "/chatC/data/users" + version + ".xml"), "users");
	}
	
	public ClientXML getXml() {
		return xml;
	}
	
	public int getFriendsOnline() {
		int online = 0;
		for (int i = xml.getUsers().size()-1; i >= 0; i-=1) {
			if (xml.getUsers().get(i).isOnline()) {
				online+=1;
			}
		}
		return online;
	}
	
	/**
	 * @return the uLogin
	 */
	protected ChatBox getuLogin() {
		return uLogin;
	}

	/**
	 * @param uLogin the uLogin to set
	 */
	protected void setuLogin(ChatBox uLogin) {
		this.uLogin = uLogin;
	}

	/**
	 * @return the pLogin
	 */
	protected ChatBox getpLogin() {
		return pLogin;
	}

	/**
	 * @param pLogin the pLogin to set
	 */
	protected void setpLogin(ChatBox pLogin) {
		this.pLogin = pLogin;
	}

	protected Rectangle getConnectButton() {
		return new Rectangle(sideBar.x, 370, 48, 18);
	}
	
	/**
	 * @return the uLoginR
	 */
	public Rectangle getuLoginR() {
		return uLoginR;
	}

	/**
	 * @param uLoginR the uLoginR to set
	 */
	public void setuLoginR(Rectangle uLoginR) {
		this.uLoginR = uLoginR;
	}

	/**
	 * @return the pLoginR
	 */
	public Rectangle getpLoginR() {
		return pLoginR;
	}

	/**
	 * @param pLoginR the pLoginR to set
	 */
	public void setpLoginR(Rectangle pLoginR) {
		this.pLoginR = pLoginR;
	}

	/**
	 * @return the inLoginScreen
	 */
	public boolean isInLoginScreen() {
		return inLoginScreen;
	}

	/**
	 * @param inLoginScreen the inLoginScreen to set
	 */
	public void setInLoginScreen(boolean inLoginScreen) {
		this.inLoginScreen = inLoginScreen;
	}

	/**
	 * @return the shouldLogin
	 */
	public boolean isShouldLogin() {
		return shouldLogin;
	}

	/**
	 * @param shouldLogin the shouldLogin to set
	 */
	public void setShouldLogin(boolean shouldLogin) {
		this.shouldLogin = shouldLogin;
	}

	/**
	 * @return the sideBar
	 */
	protected Rectangle getSideBar() {
		return sideBar;
	}

	/**
	 * @param sideBar the sideBar to set
	 */
	protected void setSideBar(Rectangle sideBar) {
		this.sideBar = sideBar;
	}

	/**
	 * @return the sendButtom
	 */
	public Rectangle getSendButtom() {
		return sendButtom;
	}

	/**
	 * @param sendButtom the sendButtom to set
	 */
	public void setSendButtom(Rectangle sendButtom) {
		this.sendButtom = sendButtom;
	}

	/**
	 * @return the lockRectangle
	 */
	public Rectangle getLockRectangle() {
		return lockRectangle;
	}

	/**
	 * @param lockRectangle the lockRectangle to set
	 */
	public void setLockRectangle(Rectangle lockRectangle) {
		this.lockRectangle = lockRectangle;
	}

	/**
	 * @return the chatBox
	 */
	public ChatBox getChatBox() {
		return chatBox;
	}

	/**
	 * @param chatBox the chatBox to set
	 */
	public void setChatBox(ChatBox chatBox) {
		this.chatBox = chatBox;
	}

	/**
	 * @return the lockPressed
	 */
	public boolean isLockPressed() {
		return lockPressed;
	}

	/**
	 * @param lockPressed the lockPressed to set
	 */
	public void setLockPressed(boolean lockPressed) {
		this.lockPressed = lockPressed;
	}

	/**
	 * @return the bigChange
	 */
	public boolean isBigChange() {
		return bigChange;
	}

	/**
	 * @param bigChange the bigChange to set
	 */
	public void setBigChange(boolean bigChange) {
		this.bigChange = bigChange;
	}

	/**
	 * @return the currentUser
	 */
	public int getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	public void setCurrentUser(int currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the contactsPressed
	 */
	public boolean isContactsPressed() {
		return contactsPressed;
	}

	/**
	 * @param contactsPressed the contactsPressed to set
	 */
	public void setContactsPressed(boolean contactsPressed) {
		this.contactsPressed = contactsPressed;
	}

	/**
	 * @return the contacts
	 */
	public Rectangle getContacts() {
		return contacts;
	}

	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(Rectangle contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return the multiUserOldSize
	 */
	public int getMultiUserOldSize() {
		return multiUserOldSize;
	}

	/**
	 * @param multiUserOldSize the multiUserOldSize to set
	 */
	public void setMultiUserOldSize(int multiUserOldSize) {
		this.multiUserOldSize = multiUserOldSize;
	}

	/**
	 * @return the expandBoxPressed
	 */
	public boolean isExpandBoxPressed() {
		return expandBoxPressed;
	}

	/**
	 * @return the somethingChanged
	 */
	public boolean isSomethingChanged() {
		return somethingChanged;
	}

	/**
	 * @return the somethingChanged
	 */
	public boolean hasSomethingChanged() {
		return somethingChanged;
	}

	/**
	 * @param somethingChanged the somethingChanged to set
	 */
	public void setSomethingChanged(boolean change) {
		somethingChanged = change;
	}

	/**
	 * @return the expandBox
	 */
	public Rectangle getExpandBox() {
		return expandBox;
	}

	/**
	 * @param expandBox the expandBox to set
	 */
	public void setExpandBox(Rectangle expandBox) {
		this.expandBox = expandBox;
	}

	public boolean expandBoxPressed() {
		return expandBoxPressed;
	}
	
	public void setExpandBoxPressed(boolean tf) {
		expandBoxPressed = tf;
	}

	public boolean isExpandBoxChanged() {
		return expandBoxChanged;
	}

	public void setExpandBoxChanged(boolean expandBoxChanged) {
		this.expandBoxChanged = expandBoxChanged;
	}

	public boolean isConnectMenu() {
		return connectMenu;
	}

	public void setConnectMenu(boolean connectMenu) {
		this.connectMenu = connectMenu;
	}

	public boolean isFirstRunSinceLogin() {
		return firstRunSinceLogin;
	}

	public void setFirstRunSinceLogin(boolean firstRunSinceLogin) {
		this.firstRunSinceLogin = firstRunSinceLogin;
	}

//	public XML getXml() {
//		return xml;
//	}
//
//	public void setXml(XML xml) {
//		this.xml = xml;
//	}

	public ClientSocket getSocket() {
		return socket;
	}

	public void setSocket(ClientSocket socket) {
		this.socket = socket;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public boolean isSetWaitChange() {
		return setWaitChange;
	}

	public void setWaitChange(boolean setWaitChange) {
		this.setWaitChange = setWaitChange;
	}
	
}
