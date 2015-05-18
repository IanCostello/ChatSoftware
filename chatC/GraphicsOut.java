package chatC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import me.iancostello.chat.ChatServer;
import me.iancostello.chat.ChatSocket;
import me.iancostello.sec.CostelloKeyPair;
import me.iancostello.util.ByteBuffer;

public class GraphicsOut extends JFrame {
	private static final long serialVersionUID = -1417201266697509030L;
	//For Updating
	private ArrayList<Rectangle> changedAreas;
	private boolean firstRun = true;
	private boolean firstPaint = true;
	private boolean firstRunForUpdate = true;
	//Vars
	private DataInterface data;
	private Rectangle clickableExpand;
	private int height;
	private int width;
	private int origHeight;
	private int origWidth;
	private Rectangle sideBar;
	private Rectangle lockRect = new Rectangle(593, 42, 38, 32);
	//Finals
	private final int WIDTH_OF_WINDOW = 725;
	private final int HEIGHT_OF_WINDOW = 450;
	private final int EXPAND_BOX_XSHIFT = 45;
	private final int SIDE_BAR_WIDTH = 50;
	private final double SIDE_BAR_PERCENT_SHIFT = 0.8;
	private final int CHAR_SIZE = 19;
	private static final String HOST = "http://127.0.0.1:8080";
	//Friends
	//Background Color
	Color backColor;
	//Debug
	private static final boolean DEBUG = true;
	//Boxes
	private ChatBox cb;
	private ChatBox uL;
	private ChatBox pL;
	private Rectangle chatBoxR;
	private Rectangle uLoginR;
	private Rectangle pLoginR;
	//Colors
	Color chatBoxGray = new Color(80, 104, 133); //Very blue gray
	Color contactGray = new Color(255, 107, 11); //Darker gray
	Color contactRed = new Color(255, 89, 63); //Stoplight red
	Color contactGreen = new Color(20, 178, 24); //StopLight green
	Color contactCircleGray = new Color(84, 84, 71); //Very Gray <-- This color needs update
	Color topBarGray = new Color (81, 106, 115); //A gray with slight hint of blue
	Color topBarContactGray = new Color (200, 200, 200); //Grey
	Color colorTopBarGray = new Color (155,155,155); //Grey
	Color defaultInfoGray = new Color (155,155,155); //Grey
	Color sendButtonGreen = new Color (130,219,136); //Grey

	//Fonts
	Font chatBoxFont = new Font(Font.SANS_SERIF, CHAR_SIZE, CHAR_SIZE);
	Font chatFont = new Font(Font.SANS_SERIF, 15, 15);
	Font titleFont = new Font(Font.MONOSPACED, 42, 42);
	Font contactFont = new Font(Font.SERIF, 25, 25);
	Font onlineFont = new Font(Font.SERIF, 10, 10);
	Font connectFont = new Font(Font.SERIF, 13, 13);
	Font friendsFont = new Font(Font.SERIF, 16, 16);
	Font loginFont = new Font(Font.SANS_SERIF, 40, 40);
	//Contacts
	private int lastUser;
	//Images
	ImageIcon contactImageIcon = getImage("/me/iancostello/chat/images/Contact.png");
	Image contactImage;
	ImageIcon santaImageIcon = getImage("/me/iancostello/chat/images/Lock.png");
	Image santaImage;
	ImageIcon sendImageIcon = getImage("/me/iancostello/chat/images/send.png");
	Image sendImage;
	ImageIcon gearImageIcon = getImage("/me/iancostello/chat/images/Settings.png");
	Image gearImage;
	//Users
	private ClientUser user;
	private boolean badLogin;
	NotificationWindow nw = new NotificationWindow();

	
	/** Main */
	//	public static void main (String[] args) {
	//		GraphicsOut go = new GraphicsOut();
	//	}

	public GraphicsOut(int version) {
		//Window Options
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		//Set Location In The Center OF Screen
		setLocation((int)screenWidth/2 - (WIDTH_OF_WINDOW/2), (int) screenHeight/2 - (HEIGHT_OF_WINDOW/2));
		setFocusTraversalKeysEnabled(false);
		//Other Setters
		setSize(725, 450);
		setResizable(false);
		setUndecorated(false);

		//For Debug
		if (version == 0) {
			setTitle("Secure Chat - Ian");
		} else if (version == 1) {
			setTitle("Secure Chat - Mark");
		}	
		//Create Data Interface
		data = new DataInterface(version);
		//Read in from the XML
		data.getXml().read();
		//For First Time Users
		if (data.getXml().getUsers().size()==0) {
			data.getXml().addUser("Welcome Bot", "welcomeBot");
			data.getXml().getUsers().get(0).addMessage("Hello! Thanks for trying out the program!", false);
			data.getXml().getUsers().get(0).addMessage("Click on the contact button to add friends!", false);
			data.getXml().getUsers().get(0).generateKeys();
		}
		//Custom exit options
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exit();
			}
		});	

		//Vars
		initVars();
		//Mouse 
		MouseHandler mh = new MouseHandler(data);
		addMouseListener(mh);
		//KeyBoard
		KeyBoardListener kb = new KeyBoardListener(data);
		addKeyListener(kb);
		//Visibility
		setVisible(true);
		//Start the Thread
		new UpdateServer().start();
		new UpdateGraphics().start();
	}

	/** Exit - Called When the Program Exits */
	public void exit() {
		//Notify Server of Logout
		data.getSocket().write("logout\n");
		//Save The Xml
		data.getXml().write();
		System.out.println("Exiting...");
		System.exit(0);
	}

	/** getImage */
	private ImageIcon getImage(String path) {
		//Loads the resource | For Jars
		ImageIcon image=null;
		URL url = getClass().getResource(path);
		image = new ImageIcon(url);
		return image;
	}

	/** Paint Method */
	public void paint(Graphics g) {
		//Test
		if (firstRun) {
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			firstRun = false;
		}
		origHeight = height;
		origWidth = width;
		height = this.getHeight();
		width = this.getWidth();
		if (data.isInLoginScreen()) {
			createLoginScreen(g);
		} else {
			if (data.getCurrentUser() >= data.getXml().getUsers().size()) {
				data.setCurrentUser(lastUser);
				data.setConnectMenu(true);
				data.getpLogin().setVisible(true);
				data.getuLogin().setVisible(true);
				data.getuLogin().setFocused(true);
				data.setExpandBoxPressed(true);
			}
			drawOverChanged(g);
			drawMessages(g);
			//Need to Update Front Stuff
			if (data.isBigChange()) {
				drawMessageBox(g);
				drawTopBar(g);
			} else if (!data.isExpandBoxPressed()){
				drawMessageBox(g);
				drawTopBar(g);	
			}
			drawSideBar(g);
			data.setBigChange(false);
		}
		//drawTopButtons(g);
	}

	public void connectMenu(Graphics g) {
		if (data.isBigChange()) {
			data.getpLogin().setBoxX(175);
			data.getuLogin().setBoxX(175);
			data.getpLoginR().x = 175;
			data.getpLoginR().x = 175;
			data.getpLogin().setProtected(false);
			data.setFirstRunSinceLogin(false); 
			//Draw Contact Text
			g.setFont(new Font(Font.MONOSPACED, 50, 50));
			g.drawString("Add a Friend", 145, 80);
			g.setFont(friendsFont);
			g.drawString("Username", 285, 150);
			g.drawString("Display Name", 280, 235);
			//Group Messages
		}

		//Draw Login Bar
		int logX = 175;
		int heightUY = 157;
		int heightPY = 247;
		int loginWidth = 290;
		//Round Rectangle
		g.setColor(Color.white);
		g.fillRoundRect(logX, heightUY, loginWidth, 50, 10, 10);
		g.fillRoundRect(logX, heightPY, loginWidth, 50, 10, 10);
		g.setColor(Color.black);
		g.setFont(loginFont);
		FontMetrics fontMet = g.getFontMetrics();
		String uLoginInfo = data.getuLogin().toString();
		String pLoginInfo = data.getpLogin().toString();
		int uLength = fontMet.stringWidth(uLoginInfo);
		int	pLength = fontMet.stringWidth(pLoginInfo);
		g.drawString(uLoginInfo, logX+(loginWidth/2)-(uLength/2), heightUY+38);
		g.drawString(pLoginInfo, logX+(loginWidth/2)-(pLength/2), heightPY+38);
		g.setColor(Color.blue);
		if (data.getuLogin().isFocused()) {
			g.drawRoundRect(logX, heightUY, loginWidth, 50, 10, 10);
		} else if (data.getpLogin().isFocused()) {
			g.drawRoundRect(logX, heightPY, loginWidth, 50, 10, 10);
		}
	}

	/** createLoginScreen */
	public void createLoginScreen(Graphics g) {
		backColor = Color.gray;
		if (data.isBigChange() || firstPaint) {
			//Draw Gradient background
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(new Color(125, 125, 125));
			GradientPaint greyTogrey = new GradientPaint(0, height/2, new Color(151, 151, 151), 0, 0, new Color(75, 69, 69));
			GradientPaint greyTogrey2 = new GradientPaint(height/2, height, new Color(75, 69, 69), 0, 0, new Color(151, 151, 151));
			g2.setPaint(greyTogrey);
			g2.fill(new Rectangle2D.Double(0, 0, width, height/2));
			g2.setPaint(greyTogrey);
			g2.fill(new Rectangle2D.Double(0, height/2, width, height));
			firstPaint = false;
			g.setFont(titleFont);
			g.setColor(Color.white);
			g.drawString("Private Secure Messaging", 60, 80);
		}

		//Draw Login Bar
		int logX = (int)(width * 0.3);
		int heightUY = (int)(height * 0.35);
		int heightPY = (int)(height * 0.55);
		int loginWidth = (int)(width * 0.4);
		//Draw Actual Boxes
		g.setColor(Color.white);
		g.fillRoundRect(logX, heightUY, loginWidth, 50, 10, 10);
		g.fillRoundRect(logX, heightPY, loginWidth, 50, 10, 10);
		g.setColor(Color.black);
		g.setFont(loginFont);
		FontMetrics fontMet = g.getFontMetrics();
		//Get Messages
		String uLoginInfo = data.getuLogin().toString();
		String pLoginInfo = data.getpLogin().toString();
		//Get Their Length
		int uLength = fontMet.stringWidth(uLoginInfo);
		int	pLength = fontMet.stringWidth(pLoginInfo);
		//Draw The Messages inside
		g.drawString(uLoginInfo, logX+(loginWidth/2)-(uLength/2), heightUY+40);
		g.drawString(pLoginInfo, logX+(loginWidth/2)-(pLength/2), heightPY+45);
		//Draw Default Info
		g.setColor(defaultInfoGray);
		if (uLoginInfo.equals("")) {
			g.drawString("Username", logX-(uLength/2)+43, heightUY+40);
		} 
		if (pLoginInfo.equals("")) {
			g.drawString("Password", logX-(pLength/2)+52, heightPY+40);
		}
		//Draw The Focused Box
		g.setColor(Color.blue);
		if (data.getuLogin().isFocused()) {
			g.drawRoundRect(logX, heightUY, loginWidth, 50, 10, 10);
			g.setColor(Color.white);
			g.drawRoundRect(logX, heightPY, loginWidth, 50, 10, 10);
		} else if (data.getpLogin().isFocused()) {
			g.drawRoundRect(logX, heightPY, loginWidth, 50, 10, 10);
			g.setColor(Color.white);
			g.drawRoundRect(logX, heightUY, loginWidth, 50, 10, 10);
		}
	}

	/** drawMessages */
	public void drawMessages(Graphics g) {
		//Check if users is empty
		if (data.getXml().getUsers().size()==0) {
			return;
		}
		//Gets all the messages with the current user
		ArrayList<String> multi = data.getXml().getUsers().get(data.getCurrentUser()).getMessages();
		//Only draw new messages if something has changed
		if (data.getMultiUserOldSize() != multi.size()) {
			//Get the user
			lastUser = data.getCurrentUser();
			//Clear the message screen
			g.setColor(Color.white);
			g.fillRect(50, 75, width-50, height-75);
			//Get the font
			g.setFont(chatFont);
			FontMetrics fontMet = g.getFontMetrics(chatFont);
			//Tracks the total number of lines
			int numLines = 0;
			int drawPointY = 80;
			//Loops through multi
			for (int i = multi.size()-1; i >=0; i-=1) {
				//makes sure the message isn't being draw off screen
				if (drawPointY < 75) {
					break;
				}
				//Keeps track of each line info
				ArrayList<String> lines = new ArrayList<String>();
				ArrayList<Integer> lengths = new ArrayList<Integer>();
				//Get the message
				String s = multi.get(i);
				String t = "";
				int length = 0;
				int end = 0;
				int totalLength = 0;
				int number = 0;
				number = Integer.parseInt(s.substring(0, 1));
				//For each work figure out its size and make sure it isn't bigger than set amount
				for (int j = 1; j < s.length(); j+=1) {
					char ch = s.charAt(j);
					end = s.length();
					//Find the next zero
					for (int k = j; k < s.length(); k+=1) {
						if (s.charAt(k) == ' ') {
							end = k;
							break;
						}
					}
					//Get the work
					String word = s.substring(j, end);
					length = fontMet.stringWidth(word);
					//If its too big add the current line and loop
					if (length + totalLength > 450) {
						lines.add(t);
						lengths.add(totalLength);
						numLines+=1;
						t = "";
						t += word;
					} else if (word.length()>40) {
						lines.add(t.substring(j, j+70));
						lengths.add(fontMet.stringWidth(t));
						t = "";
						t = t.substring(j+71, word.length());
					} else {
						t +=word;
					}
					j+=word.length();
					t+=" ";
					totalLength = fontMet.stringWidth(t);
					//start += word.length();
				}
				lines.add(t);
				lengths.add(totalLength);
				numLines +=1;

				//Everything drawing
				//Get the largest lengths
				int greatest = 0;
				for (int h = 0; h < lengths.size(); h+=1) {
					if (lengths.get(h) > greatest) {
						greatest = lengths.get(h);
					}
				}

				//update the drawpoints
				drawPointY = height - (numLines * 30) - 75 - ((multi.size()-i-1)*5);
				//drawPointY = height - (numLines * 15) - 75 - ((multi.size()-i-1)*5) - (i*10);

				//x
				int x;
				if (number == 0) {
					x = width - greatest - 50;
				} else {
					x = 70;
				}
				//Draw the Rectangle around the chat
				g.setColor(Color.black);
				g.drawRoundRect(x, drawPointY, greatest+7, 20*lines.size()+10, 10, 10);
				g.setColor(topBarContactGray);
				g.fillRoundRect(x, drawPointY, greatest+7, 20*lines.size()+10, 10, 10);
				//Loop through for each message;
				for (int k = 0; k < lines.size(); k+=1) {
					g.setColor(Color.black);
					String toPrint = lines.get(k);
					int toPrintLength = fontMet.stringWidth(toPrint);
					g.drawString(toPrint, x + ((lengths.get(k)+10)/2)-(toPrintLength/2), drawPointY + (k*20) + 20);
				}
				data.setMultiUserOldSize(multi.size());
			}
		}
	}

	public void drawTopButtons(Graphics g) {
		//Exit Buttons
		g.setColor(colorTopBarGray);
		g.fillRect(0, 0, width, 23);
		//TODO All the top buttons
		g.setColor(Color.red);
		g.fillOval(3, 3, 18, 18);
	}
	
	/** drawTopBar */
	public void drawTopBar(Graphics g) {
		//Base Color
		g.setColor(topBarGray);
		g.fillRect(0, 0, width, 75);
		//Name
		FontMetrics fontMet = g.getFontMetrics(titleFont);
		if (data.getXml().getUsers().size() > 0) {
			int length = fontMet.stringWidth(data.getXml().getUsers().get(data.getCurrentUser()).getName());
			int height = fontMet.getHeight();
			g.setFont(titleFont);
			g.setColor(topBarContactGray);
			g.drawString(data.getXml().getUsers().get(data.getCurrentUser()).getName(), (width/2)-(length/2), height + 15);
		}
		//Draw Lock
		g.drawImage(santaImage, 590, 26, 40, 44, null);
		//Draw Online Or Offline
		if (data.getXml().getUsers().get(data.getCurrentUser()).isOnline()) {
			g.setColor(contactGreen);
		} else {
			g.setColor(contactRed);
		}
		//Inside oval - Possible Contact Photos Later on
		g.fillOval(73, 27, 43, 43);
		g.setColor(contactCircleGray);
		g.fillOval(77, 31, 35, 35);
	
	}

	/** drawMessageBox */
	public void drawMessageBox(Graphics g) {
		//Message Box
		data.getChatBox().setVisible(true);
		g.setColor(chatBoxGray);
		drawRect(g, chatBoxR, 10, 10);

		//Print out box
		ArrayList<String> lines = new ArrayList<String>();
		String t = data.getChatBox().toString();
		FontMetrics fontMet = g.getFontMetrics(chatBoxFont);
		int width = data.getChatBox().getWidth() - (int)(CHAR_SIZE*0.3);
		int start=0;
		int end=t.length();
		for (int i=1; i<end; i+=1) {
			if (fontMet.stringWidth(t.substring(start,i)) >= width) {
				int index = t.lastIndexOf(' ', i-1);
				if (index > start) {
					lines.add(t.substring(start,index));
					start = index + 1;
				} else {
					lines.add(t.substring(start,i-1));
					start=i-1;
				}
			}
		}
		lines.add(t.substring(start,end));

		//Now print it out
		g.setColor(Color.white);
		g.setFont(chatBoxFont);
		int high = lines.size()-1;	
		if (high >= 1) {
			//Newest
			g.drawString(lines.get(high), data.getChatBox().getX() + (int)(CHAR_SIZE*0.3), data.getChatBox().getY() + (int)(CHAR_SIZE * 0.9) + 15 + CHAR_SIZE);
			//Second to Last
			g.drawString(lines.get(high-1), data.getChatBox().getX() + (int)(CHAR_SIZE*0.3), data.getChatBox().getY() + (int)(CHAR_SIZE * 0.9) + 5);
		} else {
			g.drawString(lines.get(high), data.getChatBox().getX() + (int)(CHAR_SIZE*0.3), data.getChatBox().getY() + (int)(CHAR_SIZE * 0.9) + 5);
		}

		//Send
		g.setColor(sendButtonGreen);
		g.fillRoundRect(data.getChatBox().getX() + data.getChatBox().getWidth() + 10, data.getChatBox().getY() + 6, 100, 50, 10, 10);
		g.drawImage(sendImage, data.getChatBox().getX() + data.getChatBox().getWidth() + 15, data.getChatBox().getY() + 14, 90, 35, null);

	}

	/** drawSideBar */
	public void drawSideBar(Graphics g) {

		backColor = Color.white;
		//Changes to Vars
		sideBar.height = height;
		clickableExpand.y = (height - 70);
		//Set Color Grey
		g.setColor(new Color(125, 125, 125));
		changedAreas.add(clickableExpand);
		if (data.expandBoxPressed()) {
			clickableExpand.x = (int) (width * SIDE_BAR_PERCENT_SHIFT) + EXPAND_BOX_XSHIFT;
			sideBar.x = (int) (width * SIDE_BAR_PERCENT_SHIFT);
			if (data.isBigChange()) {
				//Whole SideBar
				g.fillRect(0, 0, sideBar.x+10, height);
				//Actual SideBar
				g.fillRoundRect(sideBar.x, 0, SIDE_BAR_WIDTH, height, 3, 3);
				//Expand Bax
				g.fillRoundRect(clickableExpand.x, clickableExpand.y, clickableExpand.width, clickableExpand.height, 6, 6);
				//Small Line
				g.setColor(Color.white);
				g.drawLine(sideBar.x - 5, 40, sideBar.x - 5, height - 15);
			}

			if (data.isContactsPressed()) {
				if (data.isConnectMenu()) {
					connectMenu(g);
				} else {
					drawContactsMenu(g);
				}
				//connectMenu(g);
			} 
		} else {
			clickableExpand.x = EXPAND_BOX_XSHIFT;
			sideBar.x = 0;
			g.fillRect(sideBar.x, sideBar.y, SIDE_BAR_WIDTH, height);
			//g.fillRoundRect(clickableExpand.x, clickableExpand.y, clickableExpand.width, clickableExpand.height, 6, 6);
		}
		//Friends 
		g.setFont(onlineFont);
		g.setColor(Color.black);
		g.drawString("Friends On", sideBar.x + 3, height/2);
		g.setFont(friendsFont);
		FontMetrics fontMet = g.getFontMetrics();
		String s = data.getFriendsOnline() + "/" + data.getXml().getUsers().size();
		int length = fontMet.stringWidth(s);
		g.drawString(s, sideBar.x + (sideBar.width/2)-(length/2), (height/2) + 16);
		//Images
		g.drawImage(contactImage, sideBar.x-1 , 30, 50, 65, null);
		//g.drawImage(gearImage, sideBar.x-1 + 2, 120, 45, 45, null);
		//Double Var
		changedAreas.add(sideBar);
		//Update 
		data.setExpandBox(clickableExpand);
		data.setContacts(new Rectangle(sideBar.x, 30, 50, 65));
		data.setSideBar(sideBar);

	}

	public void drawContactsMenu(Graphics g) {
		g.setFont(new Font(Font.MONOSPACED, 50, 50));
		g.setColor(Color.white);
		g.drawString("Contacts", 170, 70);
		int p = 0;
		//Loops Through Each Column
		for (int i = 0; i < 6; i+=1) {
			//Loops Through Each Row
			for (int j = 0; j < 3; j+=1) {
				//Gets the top left corner
				int x = 30 + (175*j);
				int y = 100 + (50*i);
				//If its an empty user
				if (p >= data.getXml().getUsers().size()) {
					//Fills the actual rectangle
					g.setColor(new Color(80, 80, 80));
					g.fillRect(x, y, 175, 50);
					//Draws The Boundary Of Each Rectangle
					g.setColor(Color.black);
					g.drawRect(x, y, 175, 50);
				} else {
					//If its creating a user
					String name = data.getXml().getUsers().get(p).getName();
					//Fill the rectangle
					g.setColor(new Color(105,105,105));
					g.fillRect(x, y, 175, 50);
					//Create The outline
					g.setColor(Color.black);
					g.drawRect(x, y, 175, 50);
					//Fonts
					int contactSize = Math.min((150 / (int)(name.length() * 0.7)), 30);
					contactFont = new Font(Font.SERIF, contactSize, contactSize);
					g.setFont(contactFont);
					//Draw The Name
					g.drawString(name, x + 53, y+35);
					//Draw The Oval Signifying if they are off or online
					if (data.getXml().getUsers().get(p).isOnline()) {
						g.setColor(contactGreen);
					} else {
						g.setColor(contactRed);
					}
					//Inside oval - Possible Contact Photos Later on
					g.fillOval(x+3, y+3, 43, 43);
					g.setColor(contactCircleGray);
					g.fillOval(x+7, y+7, 35, 35);
					p+=1;
				}		
			}
		}
	}

	/** drawOverChanged */
	public void drawOverChanged(Graphics g) {
		g.setColor(backColor);
		//Draw Over the Whole Screen
		if (data.isInLoginScreen()) {

		} else if (data.isBigChange()) {
			g.fillRect(0, 0, width, height);
			data.setMultiUserOldSize(-1); 
		} else if (data.isConnectMenu()) {

		} else if (data.getChatBox().isFocused()) {
			g.setColor(chatBoxGray);
			drawRect(g, chatBoxR, 3, 3);
		} else {
			//Update Random Stuff
			g.fillRect(0, 0, 60, height);
			g.fillRect(0, 0, width, 75);
		}
	}

	/** Draws The Rectangle*/
	public void drawRect(Graphics g, Rectangle r) {
		g.fillRect(r.x, r.y, r.width, r.height);
	}

	/** Draws a Round Rectangle*/
	public void drawRect(Graphics g, Rectangle r, int x, int y) {
		g.fillRoundRect(r.x, r.y, r.width, r.height, x, y);
	}

	/** Init Place For Most Vars*/
	public void initVars() {
		//Images
		contactImage = contactImageIcon.getImage();
		santaImage = santaImageIcon.getImage();
		sendImage = sendImageIcon.getImage();
		gearImage = gearImageIcon.getImage();
		//Height and Width
		height = this.getHeight();
		width = this.getWidth();
		//For overlaying
		origHeight = height;
		origWidth = width;
		//Rectangles
		clickableExpand = new Rectangle(46, height - 70, 12, 30);
		data.setExpandBox(clickableExpand);
		changedAreas = new ArrayList<Rectangle>();
		sideBar = new Rectangle(0, 0, SIDE_BAR_WIDTH, height);
		chatBoxR = new Rectangle(57, 385, width - 180, height - 390);
		uLoginR = new Rectangle((int)(width * 0.3), (int)(height * 0.35), (int)(width * 0.4), 50);
		pLoginR = new Rectangle((int)(width * 0.3), (int)(height * 0.55), (int)(width * 0.4), 50);
		data.setLockRectangle(lockRect);
		data.setuLoginR(uLoginR);
		data.setpLoginR(pLoginR);
		//ChatBoxes
		cb = new ChatBox(chatBoxR, false, CHAR_SIZE,-1);
		cb.setVisible(false);
		uL = new ChatBox(uLoginR, false, 15, 12);
		uL.setVisible(true);
		pL = new ChatBox(pLoginR, true, 15, 14);
		pL.setVisible(true);
		data.setuLogin(uL);
		data.setpLogin(pL);
		data.setChatBox(cb);
		data.setSendButtom(new Rectangle(data.getChatBox().getX() + data.getChatBox().getWidth() + 10, data.getChatBox().getY() + 11, 100, 40));
	}

	public class UpdateGraphics extends Thread {
		public void run() {
			while (true) {
				//Graphics
				if (data.hasSomethingChanged()) {
					data.setSomethingChanged(false);
					repaint();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public class UpdateServer extends Thread {
		int count = 101;

		public void run() {
			if (firstRunForUpdate) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				firstRunForUpdate = false;
			}
			//Start Local ChatServer For Debug
			InetAddress serverIP = null;

			try {
				if (Main.DEBUG) {
					serverIP = InetAddress.getByName("127.0.0.1");
				} else {
					serverIP = InetAddress.getByName("iancostello.me");
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			//Create Client Socket
			ClientSocket socket = new ClientSocket(serverIP, ChatServer.serverPort);
			data.setSocket(socket);
			//data.getSocket().login();

			while (true) {
				//Login
				processInput();
				//Long Changes
				if (!data.isInLoginScreen()) {
					if (data.getChatBox().shouldWrite()) {
						data.getChatBox().setShouldWrite(false);
						/** This is where messages are sent from */
						if (!data.getChatBox().toString().equals("")) {
							//Get the message
							String msg = data.getChatBox().toString();
							ClientUser user = data.getXml().getUsers().get(data.getCurrentUser());
							//Encrypt it
							CostelloKeyPair kp = new CostelloKeyPair();
							ByteBuffer encryptedMessage = new ByteBuffer();
							encryptedMessage.appendHex(kp.encrypt(msg, user.getPublicKey(), user.getPublicMod()));
							//Add it to the UI
							data.getXml().getUsers().get(data.getCurrentUser()).addMessage(msg, true);
							//Send data to server
							ByteBuffer bBuf = new ByteBuffer();
							bBuf.append("msg ").append(user.getUsername()).append(' ').append(encryptedMessage).append('\n');
							data.getSocket().write(bBuf);
							data.getChatBox().clear();
						}
						data.setSomethingChanged(true);
					}
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void processInput() {
			// Read line from server. Null if no input available.
			ByteBuffer bBuf = null;
			boolean isConnected = data.getSocket().isConnected();
			if (isConnected) {
				try {
					bBuf = data.getSocket().read(0);
				} catch (IOException e) {
					isConnected = false;
				}
			}

			// If server connection dies after login, reconnect and login again.
			//				   XML xml = data.getXml();
			//				   if (!isConnected && !xml.isInLoginScreen()) {
			//				      try {
			//				           socket.connect();
			//				           socket.write("login "+ xml.getLocalUser() + " " + xml.getLocalUserPassword() + "\n");
			//				      } catch (IOException e)
			//				      }
			//				   }

			if (bBuf != null) {
				// login ok
				// create ok
				if (bBuf.startsWith("login ") || bBuf.startsWith("create ")) {
					if (bBuf.equals("login ok") || bBuf.equals("create ok")) {
						String localUser = data.getuLogin().toString();
						data.getXml().setLocalUser(localUser);
						data.getSocket().setLocalUser(localUser);
						String capitalized = localUser.substring(0, 1).toUpperCase() + localUser.substring(1);
						setTitle("Secure Chat - " + capitalized);
						data.getXml().write();
						data.setInLoginScreen(false);
						//Send Friends
						ByteBuffer friends = new ByteBuffer();
						friends.append("friends");
						ArrayList<ClientUser> users = data.getXml().getUsers();
						int size = users.size();
						for (int i = size -1; i >= 0; i-=1) {
							ClientUser user = users.get(i);
							String username = user.getUsername();
							if (username.equals("welcomeBot")) {
								size-=1;
							} else {
								friends.append(' ').append(username);
							}
						}
						friends.append('\n');
						if (size>0) {
							data.getSocket().write(friends);
						}
						data.getpLogin().clear();
						data.getuLogin().clear();
					} else {
						data.setLoginTime(0);
						//TODO Display
						System.out.println(bBuf.toString());
						String msg = bBuf.toString();
						JOptionPane.showMessageDialog(new JFrame(), msg, "Error", JOptionPane.ERROR_MESSAGE);
					}
					data.setWaitChange(true);
					// Message
					//   msg fromUser TheMessage
				} else if (bBuf.startsWith("msg ")) {
					//					ByteBuffer data = new ByteBuffer();
					//					data.append(bBuf);
					//					data.hexToBytes();
					//					ByteBuffer decrypted = new ByteBuffer();
					//					decrypted.append(rsa.decryptHex(data, privKey, privMod));
					//					System.out.println(decrypted.toString());
					//Get Who its From
					bBuf.moveStart(bBuf.start()+4);
					ByteBuffer fromWho = new ByteBuffer();
					bBuf.getToken(fromWho, ByteBuffer.WHITE_SPACE);
					String messageSender = fromWho.toString();
					//Get The User
					ClientUser user = data.getXml().getUser(messageSender);
					if (user!=null) {
						//Decrypt
						CostelloKeyPair kp = new CostelloKeyPair();
						ClientUser localUser = data.getXml().getLocalUser();
						ByteBuffer hex = new ByteBuffer();
						hex.append(bBuf);
						ByteBuffer message = new ByteBuffer();
						message.append(kp.decryptHex(hex, localUser.getPrivKey(), localUser.getPrivMod()));
						user.addMessage(message.toString(), false);
						String capitalized = messageSender.substring(0, 1).toUpperCase() + messageSender.substring(1);
						if (!isFocused() || !data.getXml().getUsers().get(data.getCurrentUser()).getUsername().equalsIgnoreCase(messageSender)) {
							nw.createWindow(" New Message From " + capitalized);
						}
					}
					data.setWaitChange(true);

					// Active
					//   active mark markus
				} else if (bBuf.startsWith("active ")) {
					bBuf.moveStart(bBuf.start()+7);
					ByteBuffer token = new ByteBuffer();
					while (bBuf.getToken(token, ByteBuffer.WHITE_SPACE)) {
						ClientUser user = data.getXml().getUser(token.toString());
						if (user!=null) {
							user.setActive(true);
						} else {
							System.out.println("Server Updated an Active User, But User is not a Friend");
						}
					}
					data.setWaitChange(true);

					// Inactive
					//   inactive mark markus
				} else if (bBuf.startsWith("inactive ")) {
					bBuf.moveStart(bBuf.start()+9);
					ByteBuffer token = new ByteBuffer();
					while (bBuf.getToken(token, ByteBuffer.WHITE_SPACE)) {
						ClientUser user = data.getXml().getUser(token.toString());
						if (user!=null) {
							user.setActive(false);
						}
					}
					data.setSomethingChanged(true);

					// Add Friend response:
					//   addFriend mark ok online
				} else if (bBuf.startsWith("addFriend ")) {
					bBuf.moveStart(bBuf.start() +10);
					ByteBuffer token = new ByteBuffer();
					bBuf.getToken(token, ByteBuffer.WHITE_SPACE);
					String status = token.toString();
					bBuf.getToken(token, ByteBuffer.WHITE_SPACE);
					//String user = token.toString();
					if (!status.equals("ok")) {
						bBuf.moveStart(0);
						System.out.println(bBuf);
					} else {
						ClientUser newFriend = new ClientUser(); 
						newFriend.setName(data.getpLogin().toString());
						newFriend.setUsername(data.getuLogin().toString());
						bBuf.getToken(token, ByteBuffer.WHITE_SPACE);
						boolean active = token.equals("active");
						newFriend.setActive(active);
						bBuf.getToken(token, ByteBuffer.WHITE_SPACE);
						String pubKey = token.toString();
						bBuf.getToken(token, ByteBuffer.WHITE_SPACE);
						String pubMod = token.toString();
						newFriend.setPublicKey(pubKey);
						newFriend.setPublicMod(pubMod);
						data.getXml().addUser(newFriend);
						data.getXml().write();
					}
					data.setWaitChange(true);
				}
				data.setSomethingChanged(true);
			}
		}
	}
}


