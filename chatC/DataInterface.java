package chatC;

import java.awt.Rectangle;

/** DataInterface
 * Storage Of All Local Variables
 */
public class DataInterface {
	private boolean expandBoxPressed; //Whether the entire screen has moved over
	private Rectangle expandBox; //Its locations
	private Rectangle sendButton; //The sendbutton
	private Rectangle lockRectangle; //Obselete
	private ChatBox chatBox; //ChatBox
	private ChatBox uLogin; //User Login Box
	private ChatBox pLogin; //Password Login Box
	private boolean lockPressed; //Whether the lock has been pressed or not
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
	
	protected DataInterface(int version) {
		xml = new ClientXML((System.getProperty("user.home") + "/chatC/data/users" + version + ".xml"), "users");
	}
	
	protected ClientXML getXml() {
		return xml;
	}
	
	protected int getFriendsOnline() {
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
	protected Rectangle getuLoginR() {
		return uLoginR;
	}

	/**
	 * @param uLoginR the uLoginR to set
	 */
	protected void setuLoginR(Rectangle uLoginR) {
		this.uLoginR = uLoginR;
	}

	/**
	 * @return the pLoginR
	 */
	protected Rectangle getpLoginR() {
		return pLoginR;
	}

	/**
	 * @param pLoginR the pLoginR to set
	 */
	protected void setpLoginR(Rectangle pLoginR) {
		this.pLoginR = pLoginR;
	}

	/**
	 * @return the inLoginScreen
	 */
	protected boolean isInLoginScreen() {
		return inLoginScreen;
	}

	/**
	 * @param inLoginScreen the inLoginScreen to set
	 */
	protected void setInLoginScreen(boolean inLoginScreen) {
		this.inLoginScreen = inLoginScreen;
	}

	/**
	 * @return the shouldLogin
	 */
	protected boolean isShouldLogin() {
		return shouldLogin;
	}

	/**
	 * @param shouldLogin the shouldLogin to set
	 */
	protected void setShouldLogin(boolean shouldLogin) {
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
	protected Rectangle getSendButton() {
		return sendButton;
	}

	/**
	 * @param sendButtom the sendButtom to set
	 */
	protected void setSendButtom(Rectangle sendButton) {
		this.sendButton = sendButton;
	}

	/**
	 * @return the lockRectangle
	 */
	protected Rectangle getLockRectangle() {
		return lockRectangle;
	}

	/**
	 * @param lockRectangle the lockRectangle to set
	 */
	protected void setLockRectangle(Rectangle lockRectangle) {
		this.lockRectangle = lockRectangle;
	}

	/**
	 * @return the chatBox
	 */
	protected ChatBox getChatBox() {
		return chatBox;
	}

	/**
	 * @param chatBox the chatBox to set
	 */
	protected void setChatBox(ChatBox chatBox) {
		this.chatBox = chatBox;
	}

	/**
	 * @return the lockPressed
	 */
	protected boolean isLockPressed() {
		return lockPressed;
	}

	/**
	 * @param lockPressed the lockPressed to set
	 */
	protected void setLockPressed(boolean lockPressed) {
		this.lockPressed = lockPressed;
	}

	/**
	 * @return the bigChange
	 */
	protected boolean isBigChange() {
		return bigChange;
	}

	/**
	 * @param bigChange the bigChange to set
	 */
	protected void setBigChange(boolean bigChange) {
		this.bigChange = bigChange;
	}

	/**
	 * @return the currentUser
	 */
	protected int getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	protected void setCurrentUser(int currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the contactsPressed
	 */
	protected boolean isContactsPressed() {
		return contactsPressed;
	}

	/**
	 * @param contactsPressed the contactsPressed to set
	 */
	protected void setContactsPressed(boolean contactsPressed) {
		this.contactsPressed = contactsPressed;
	}

	/**
	 * @return the contacts
	 */
	protected Rectangle getContacts() {
		return contacts;
	}

	/**
	 * @param contacts the contacts to set
	 */
	protected void setContacts(Rectangle contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return the multiUserOldSize
	 */
	protected int getMultiUserOldSize() {
		return multiUserOldSize;
	}

	/**
	 * @param multiUserOldSize the multiUserOldSize to set
	 */
	protected void setMultiUserOldSize(int multiUserOldSize) {
		this.multiUserOldSize = multiUserOldSize;
	}

	/**
	 * @return the expandBoxPressed
	 */
	protected boolean isExpandBoxPressed() {
		return expandBoxPressed;
	}

	/**
	 * @return the somethingChanged
	 */
	protected boolean isSomethingChanged() {
		return somethingChanged;
	}

	/**
	 * @return the somethingChanged
	 */
	protected boolean hasSomethingChanged() {
		return somethingChanged;
	}

	/**
	 * @param somethingChanged the somethingChanged to set
	 */
	protected void setSomethingChanged(boolean change) {
		somethingChanged = change;
	}

	/**
	 * @return the expandBox
	 */
	protected Rectangle getExpandBox() {
		return expandBox;
	}

	/**
	 * @param expandBox the expandBox to set
	 */
	protected void setExpandBox(Rectangle expandBox) {
		this.expandBox = expandBox;
	}

	protected boolean expandBoxPressed() {
		return expandBoxPressed;
	}
	
	protected void setExpandBoxPressed(boolean tf) {
		expandBoxPressed = tf;
	}

	protected boolean isExpandBoxChanged() {
		return expandBoxChanged;
	}

	protected void setExpandBoxChanged(boolean expandBoxChanged) {
		this.expandBoxChanged = expandBoxChanged;
	}

	protected boolean isConnectMenu() {
		return connectMenu;
	}

	protected void setConnectMenu(boolean connectMenu) {
		this.connectMenu = connectMenu;
	}

	protected boolean isFirstRunSinceLogin() {
		return firstRunSinceLogin;
	}

	protected void setFirstRunSinceLogin(boolean firstRunSinceLogin) {
		this.firstRunSinceLogin = firstRunSinceLogin;
	}

//	protected XML getXml() {
//		return xml;
//	}
//
//	protected void setXml(XML xml) {
//		this.xml = xml;
//	}

	protected ClientSocket getSocket() {
		return socket;
	}

	protected void setSocket(ClientSocket socket) {
		this.socket = socket;
	}

	protected long getLoginTime() {
		return loginTime;
	}

	protected void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	protected boolean isSetWaitChange() {
		return setWaitChange;
	}

	protected void setWaitChange(boolean setWaitChange) {
		this.setWaitChange = setWaitChange;
	}
	
}
