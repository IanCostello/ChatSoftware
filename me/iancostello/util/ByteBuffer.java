/** ByteBuffer
 * 
 * 
 */
package me.iancostello.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class ByteBuffer {
	private final int FILE_BLOCK_SIZE = 4096;
	public static final ByteBuffer WHITE_SPACE = new ByteBuffer(" \n\r\t\u00a0");
	public static final ByteBuffer COLON_SEPARATOR = new ByteBuffer(": \n\r\t\u00a0");
	public static final ByteBuffer CRLF = new ByteBuffer("\n\r");
	public static final Charset UTF8 = Charset.forName("utf-8");
	private static final byte[] NULL_BUF = new byte[0];
	private int start;
	private int end;
	private byte[] buf;
	
	/** Constructor */
	public ByteBuffer() {
		start = end = 0;
		buf = NULL_BUF;
	}
	
	/** Constructor */
	public ByteBuffer(String s) {
		start = 0;
		end = s.length();
		buf = s.getBytes();
	}
	
	/** init
	  * Initializes a buffer 
	  * @param byte[] buf2
	  * @param int start2
	  * @param int end2
	  * @return this
	  */
	public ByteBuffer init(byte[] buf2, int start2, int end2) {
		ensureCapacity(end2 - start2);
		start = end = 0;
		while (start2 < end2) {
			buf[end++] = buf2[start2++];
		}
		return this;
	}
	
	/** toString */
	@Override
	public String toString() {
		return new String(buf, start, end - start);
	}
	
	public void moveStart(int i) {
		start = i;
	}
	
	/** start
	 * 	@return int start
	 */
	public int start() {
		return start;
	}
	
	/** end
	 *  @return int end
	 */
	public int end() {
		return end;
	}
	
	/** clear
	 * 
	 */
	public ByteBuffer clear() {
		start = end = 0;
		return this;
	}
	
	/** length
	 *  @return int length
	 */
	public int length() {
		return end-start;
	}
	
	/** getBytes
	 *  @return byte[] buf
	 */
	public byte[] getBytes() {
		return buf;
	}

	/** toString */
	public byte[] toByteArray() {
		return new String(buf, start, end - start).getBytes();
	}
	
	/** toString */
	public boolean toBoolean() {
		String  s = new String(buf, start, end - start);
		if (s.equals("t")) {
		    return true;
		} else {
			return false;
		}
	}
	
	/** ensureCapacity 
	  * Grow the buffer to at least the size specified
	  * @param int size
	  */
	public void ensureCapacity(int size) {
		if (buf.length < size) {
			size = Math.max(size, buf.length * 2 + 64);
			byte[] newBuf = new byte[size];
			System.arraycopy(buf, start, newBuf, start, end - start);
			buf = newBuf;
		}	
	}
	
	/** read 
	 * Read data from a file.
	 * @param File file
	 * */
	public void read(File file) throws IOException {
		FileInputStream in = null;
		start = 0;
		ensureCapacity(Math.max(1024, (int) file.length()+512));
		try {
			in = new FileInputStream(file);
			while (true) {
				int maxNumRead = buf.length - end;
				if (maxNumRead < 512) {
					ensureCapacity(end + FILE_BLOCK_SIZE);
					maxNumRead = buf.length - end;
				}
				int numRead = in.read(buf, end, maxNumRead);
				if (numRead < 0) {
					break;
				}
				end += numRead;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + file + " does not exist!");
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/** readURL
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public boolean readURL(URL url) throws IOException {
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		String usernamePassword = Base64.encode("ian:123456");	
		urlConnection.addRequestProperty("authorization","Basic "+usernamePassword);
		start = end = 0;
		
		int responseCode = urlConnection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			return false;
		} 
		InputStream in = null;
		start = end = 0;
		try {
			in = urlConnection.getInputStream();
			while (true) {
				int end2 = end + in.available();
				if (end2 + 1024 > buf.length) {
					ensureCapacity(end2 + 4096);
				}
				int readNum = in.read(buf, end, buf.length-end);
				if (readNum<=0) break;
				end += readNum;
			}
			
		} finally {
			if (in!=null) in.close();
		}
		return true;
	}
	
	/** read
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public boolean read(InputStream in) throws IOException {
		start = end = 0;
		try {
			while (true) {
				int end2 = end + in.available();
				if (end2 + 1024 > buf.length) {
					ensureCapacity(end2 + 4096);
				}
				int readNum = in.read(buf, end, buf.length-end);
				if (readNum<=0) break;
				end += readNum;
			}
			
		} finally {
			if (in!=null) in.close();
		}
		return true;
	}
	
	/** write
	  * Writes data to a file
	  * @param File file
	  */
	public void write(File file) throws IOException {
		FileOutputStream out = null;
		try {
			file.getParentFile().mkdirs();
			out = new FileOutputStream(file);
			out.write(buf, start, end - start);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/** indexOf
	  * @param byte ch
	  * @return int index. the location of a character
	  */
	public int indexOf(byte ch) {
		for (int i = start; i < end; i+=1) {
			if (buf[i] == ch) {
				return i;
			}
		}
		return -1;
	}
	
	/** indexOf
	  * @param char ch
	  * @return int index. the location of a character
	  */
	public int indexOf(char ch) {
		for (int i = start; i < end; i+=1) {
			if (buf[i] == ch) {
				return i;
			}
		}
		return -1;
	}
	
	
	/** indexOf 
	  * @param String s
	  */
	public int indexOf(String s) {
		for (int i = start; i < end; i+=1) {
			if (buf[i] == s.charAt(0)) {
				for(int j = i; i < end; i+=1) {
					if(buf[i] == s.charAt(j)) {
						return i;
					}
				}
			}
		}
		return -1;
	}
	
	
	/** endOf 
	  * @param String s
	  * DOESNT WORK
	  */
	public int endOf(String s) {
		for (int i = start; i < end; i+=1) {
			if (buf[i] == s.charAt(0)) {
				for(int j = i; j < s.length(); i+=1) {
					if (buf[j] == s.charAt(j)) {
						return i + j - 1;
					}
				}
			}
		}
		return -1;
	}
	/** moveEnd
	 * moves the end
	 * @param i
	 */
	public void moveEnd(int i) {
		end = i;
	}
	
	/** readLine
	 *  Read through next CRLF
	 *  @param ByteBuffer token
	 *  @return true if successful
	 */
	//public void readLine(ByteBuffer ) {
		
    //}
	
	/** getToken
	  * Moves the start to the next token and loads the token with the parsed value
	  * @param ByteBuffer token
	  * @param ByteBuffer breakChar
	  * @return true if successful
	  */
	public boolean getToken(ByteBuffer token, ByteBuffer breakChar) {
		while ((start < end) && (WHITE_SPACE.indexOf(buf[start]) >= 0)) {
			start +=1;
		}
		if (start >= end) {
			return false;
		}
		int i0 = start;
		while (start < end) {
			if (breakChar.indexOf(buf[start++]) >= 0) {
				token.init(buf, i0, start-1);
				return true;
			}
		}
		token.init(buf, i0, start);
		return true;
	}
	
	/** getNextEndLine */ 
	public boolean getNextEndLine() {
		while (!(WHITE_SPACE.indexOf(buf[start]) >= 0)) {
			if (start <= end) {
				return false;
			} 
			start +=1;
		}
		return true;
	}
	
	/** append
	  * @param ByteBuffer sbuf2
	  * @return this
	  */
	public ByteBuffer append(ByteBuffer sBuf2) {
		append(sBuf2.buf,sBuf2.start, sBuf2.end);
		return this;
	}
	
	/** append
	  * @param String s
	  * @return this
	  */
	public ByteBuffer append(String s) {
		byte[] buf2 = s.getBytes();
		append(buf2, 0, buf2.length);
		return this;
	}
	
	/** append
	  * @param int m
	  * @return this
	  */
	public ByteBuffer append(int m) {
		append(String.valueOf(m));
		return this;
	}
	
	/** append
	  * @param byte ch
	  * @return this
	  */
	public ByteBuffer append(byte ch) {
		if (end >= buf.length) {
			ensureCapacity(end + 16);
		}
		buf[end++] = ch;
		return this;
	}
	
	/** append
	  * @param char ch
	  * @return this
	  */
	public ByteBuffer append(char ch) {
		if (end+1 >= buf.length) {
			ensureCapacity(buf.length + 16);
		}
		if (ch<0x80) {
			buf[end++] = (byte)ch;
		} else if (ch<0x800) {
			buf[end++] = (byte)(0xc0+ch/64);
			buf[end++] = (byte)(0*80+ch%64);
		} else {
			buf[end++] = (byte)(0xe0+ch/4096);
			buf[end++] = (byte)(0x80+(ch%4096)/64);
			buf[end++] = (byte)(0x80+ch/64);
		}
		return this;
	}
	
	/** append
	 * 	@param byte[] buf2
	 */
	public ByteBuffer append(byte[] buf2) {
		append(buf2,0,buf2.length);
		return this;
	}
	
	/** append
	  * @param byte[] buf2
	  * @param int start2
	  * @param int end2 
	  * @return this
	  */
	public ByteBuffer append(byte[] buf2, int start2, int end2) {
		ensureCapacity(end + (end2 - start2));
		while (start2 < end2) {
			buf[end++] = buf2[start2++];
		}
		return this;
	}
	
	public ByteBuffer appendHex(byte ch) {
		int b0 = (ch>>4) & 0xF;
		if (b0 < 10) {
			append((byte) ('0' + b0));
		} else {
			append((byte) ('A' + b0-10));
		}
		b0 = ch & 0xF;
		if (b0 < 10) {
			append((byte) ('0' + b0));
		} else {
			append((byte) ('A' + b0-10));
		}
		return this;
	}
	
	public ByteBuffer appendHex(byte[] buf) {
		for (int i = 0; i < buf.length; i+=1) {
			appendHex(buf[i]);
		}
		return this;
	}
	
	public ByteBuffer appendHex(ByteBuffer bBuf) {
		int end = bBuf.end;
		byte[] buf2 = bBuf.getBytes();
		for (int i = bBuf.start; i < end; i+=1) {
			appendHex(buf2[i]);
		}
		return this;
	}
	
	public ByteBuffer hexToBytes() {
		int top = start;
		for (int i = start; i < end; ) {
			byte ch = buf[i++];
			int x = 0;
			if (('0'<=ch)&&(ch<='9')) {
				x = ch-'0';
			} else if (('A'<=ch)&&(ch<='F')) {
				x = ch-'A'+10;
			} else if (('a'<=ch)&&(ch<='f')) {
				x = ch-'a'+10;
			} else {
				return null;
			}
			x = x*16;
			if (i==end) return null;
			
			ch = buf[i++];
			if (('0'<=ch)&&(ch<='9')) {
				x += ch-'0';
			} else if (('A'<=ch)&&(ch<='F')) {
				x += ch-'A'+10;
			} else if (('a'<=ch)&&(ch<='f')) {
				x += ch-'a'+10;
			} else {
				return null;
			}
			//char[] tempCh = Character.toChars(x);
			buf[top++] = (byte)x;
		}
		end = top;
		return this;
	}
	
	/** intValue
	  * @param ByteBuffer
	  * @return int
	  */
	public int intValue() {
		int value = 0;
		for (int i = start; i < end; i+=1) {
			byte ch = buf[i];
			if (('0' <= ch) && (ch <= '9')) {
				value = value * 10 + ch - '0';
			} else {
				return Integer.MIN_VALUE;
			}
		}
		return value;
	}
	
	/** startsWith 
	 * @param byte ch
	 * @return true/false
	 */
	public boolean startsWith(char ch) {
		return ((start < end) && (buf[start] == ch));
	}
	
	/** startsWith */
	public boolean startsWith(ByteBuffer str2,int pos1) { return startsWith(pos1,str2.buf,str2.start(),str2.end()); }
	public boolean startsWith(ByteBuffer str2) { return startsWith(start(),str2.buf,str2.start(),str2.end()); }
	public boolean startsWith(String str,int pos1) { return startsWith(pos1,str.getBytes(UTF8),0,str.length()); }
	public boolean startsWith(String str) { return startsWith(start(),str.getBytes(UTF8),0,str.length()); }
	public boolean startsWith(byte[] buf2,int pos2,int end2) { return startsWith(start(),buf2,pos2,end2); }
	
	/** startsWith
	 * 		@param int pos1. Index within String8.
	 * 		@param byte[] buf2. String to be matched
	 * 		@param int pos2. Start index in string to be matched.
	 * 		@param int end2. End index in string to be matched.
	 */
	public boolean startsWith(int pos1,byte[] buf2,int pos2,int end2) {
		int len2 = end2-pos2;
		if ((end()-pos1) < len2) return false;
		while (len2-- > 0) {
			if (buf[pos1++] != buf2[pos2++]) return false;
		}
		return true;
	}

	/** equals 
	 * 		HashMap uses this version
	 *  	Required override for using HashMap<String16,Object>
	 */
	@Override 
	public boolean equals(Object obj) {
		if (obj instanceof ByteBuffer) {
			ByteBuffer str = (ByteBuffer)obj;
			return (compareTo(str.buf,str.start(),str.end())==0);
		} else if (obj instanceof String) {
			String str = (String)obj;
			return (compareTo(str.getBytes(UTF8),0,str.length())==0);
		} else {
			throw new Error ("Can't Compare these Object Types?");
		}
	}
	
	/** compareTo
	 * 
	 *		Warning! This MUST be symmetrical or quickSort can get into infinite loop
	 *		In particular, expect 'obj.compare2(obj2) == -obj2.compareTo(obj)'
	 *
	 *		@return (('this' prior to 'obj') ? -delta : (equal() ? 0 : +delta))
	 */
	public int compareTo(ByteBuffer str) { 
		return compareTo(str.buf,str.start(),str.end()); 
	}
	
	/** compareTo
	 * 
	 *		Warning! This MUST be symmetrical or quickSort can get into infinite loop
	 *		In particular, expect 'obj.compare2(obj2) == -obj2.compareTo(obj)'
	 *
	 *		@return (('this' prior to 'obj') ? -delta : (equal() ? 0 : +delta))
	 */
	public int compareTo(byte[] buf2,int pos2,int end2) {
		int len2 = end2-pos2;
		int len = Math.min(length(),len2);
		int pos1 = start();
		while (len-- > 0) {
			int delta = buf[pos1++]-buf2[pos2++];
			if (delta != 0) return delta;
		}
		return (length()-len2);
	}

	
	/** subset
	 *  @param int start2
	 *  @param int end2
	 *  @return ByteBuffer
	 */
	public ByteBuffer subset(int start2,int end2) {
		ByteBuffer bb = new ByteBuffer();
		bb.init(buf, start2, end2);
		return bb;
	}
		
}
