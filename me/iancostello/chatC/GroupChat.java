package me.iancostello.chatC;

import java.util.ArrayList;

/** GroupChat
 * Support For Group Chats
 * Not Fully Implemented
 */
public class GroupChat {
	private String nameOfChat;
	private ArrayList<String> users = new ArrayList<String>();
	private ArrayList<GroupMessage> messages = new ArrayList<GroupMessage>();
	
	/** Blank Constructor */
	public GroupChat() {
		
	}
	
	public ArrayList<String> getMostRecentMessages(int numMessages) {
		ArrayList<String> recentMessages = new ArrayList<String>();
		System.arraycopy(messages, (messages.size()-numMessages-1), messages, 0, numMessages);
		return recentMessages;
	}
	
	/** addUserToChat */
	public void addUserToChat(String user) {
		users.add(user);
	}
	
	public void removeUserFromChat(String user) {
		for (int i = users.size(); i >= 0; i-=1) {
			if (users.get(i).equalsIgnoreCase(user)) {
				users.remove(i);
				break;
			}
		}
	}
	
	/** addMessage */
	public void addMessage(String sender, String message) {
		messages.add(new GroupMessage(sender, message));
	}
	
	/** getNameOfChat */
	public String getNameOfChat() {
		return nameOfChat;
	}

	/** setNameOfChat */
	public void setNameOfChat(String nameOfChat) {
		this.nameOfChat = nameOfChat;
	}

	/** GroupMessage*/
	public class GroupMessage {
		String user;
		String message;

		/** Constructor */
		public GroupMessage(String user, String message) {
			this.user = user;
			this.message = message;
		}
		
		/** getUser */
		public String getUser() {
			return user;
		}
		
		/** getMessage*/
		public String getMessage() {
			return message;
		}
	}
}
