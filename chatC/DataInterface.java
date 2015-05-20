package chatC;

import java.awt.Rectangle;
import java.util.ArrayList;

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
	private boolean expandBoxChanged; //If the expand button is pressed
	private boolean somethingChanged; //If the graphics thread needs to update
	private Rectangle uLoginR; //The Drawing Info of One of the Input Boxes
	private Rectangle pLoginR; //The Drawing Info of One of the Other Input Boxes
	private boolean inLoginScreen = true; //Whether the current screen is the login screen
	private boolean shouldLogin; //Whether a login or a create should be sent
	private boolean bigChange; //If the entire screen should be redrawn
	private int currentUser; //The current user 
	private boolean contactsPressed; //Whether or not the contacts button is pressed
	private Rectangle contacts; //Where the contacts image is drawn
	private int multiUserOldSize; //The old size of the messages array of the current user. Used for performance
	private Rectangle sideBar; //Where the Sidebar should be drawn
	private boolean connectMenu; //If the user has pressed the connect menu
	private boolean firstRunSinceLogin = true; //Used to update the input boxes
	private ClientXML xml; //Local Data Storage
	private ClientSocket socket; //Used to talk with the server
	private long loginTime; //Time to login (For Timeouts)
	private ArrayList<String> peopleToAddToGroup = new ArrayList<String>(); //Not currently implemented
	private boolean groupChatCreatePressed; //Not currently implemented
	
	protected DataInterface(int version) {
		xml = new ClientXML((System.getProperty("user.home") + "/chatC/data/users" + version + ".xml"), "users", this);
	}
	
	protected ArrayList<String> getUsersToAdd() {
		return peopleToAddToGroup;
	}
	
	protected void addPersonToGroupSelect(String s) {
		peopleToAddToGroup.add(s);
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

	public boolean isGroupChatCreatePressed() {
		return groupChatCreatePressed;
	}

	public void setGroupChatCreatePressed(boolean groupChatCreatePressed) {
		this.groupChatCreatePressed = groupChatCreatePressed;
	}
	
}
