package me.iancostello.util;

public class Base64 {
	/** encode
	 * 		@param String
	 * 		@return String
	 */
	public static String encode(String s) {
		byte[] buf = s.getBytes();
		byte[] bBuf = encode(buf,0,buf.length);
		return new String(bBuf);
	}
	
    /** encode
     * 		Encode BASE64 bytes
     * 
 	 * 		Most From:
	 * 			http://en.wikipedia.org/wiki/Basic_access_authentication
	 * 			http://en.wikipedia.org/wiki/Base64
	 * 			https://docs.oracle.com/javase/8/docs/api/java/util/Base64.html
	 * 
     * 		@param byte[] buf
     * 		@param int start
     * 		@param int end
     * 		@param byte[] out
     */
    public static byte[] encode(byte[] buf, int start, int end) {
    	int inputLen = end-start;
    	if (inputLen<=0) return new byte[0];
    	int numTriplet = 1+((inputLen-1)/3);
    	int numPad = numTriplet*3 - inputLen;
     	byte[] oBuf = new byte[numTriplet*4];
    	int oIndex=0;
    			
    	while (start < end) {
    		byte ch0=buf[start++];
    		byte ch1 = (start>=end) ? 0 : buf[start++];
    		byte ch2 = (start>=end) ? 0 : buf[start++];
    		int value = ((ch0&0xff) << 16) | ((ch1&0xff) << 8) | (ch2&0xff);
    		
    		for (int j=3; j>=0; j-=1) {
    			int ch = value&0x3f;
    			value = value >> 6;
    		
    			if ((0<=ch)&&(ch<26)) {
    				oBuf[oIndex+j] = (byte)('A'+ch);
    			} else if ((26<=ch)&&(ch<52)) {
    				oBuf[oIndex+j] = (byte)('a'+ch-26);
    			} else if ((52<=ch)&&(ch<62)) {
    				oBuf[oIndex+j] = (byte)('0'+ch-52);
    			} else {
    				oBuf[oIndex+j] = (ch==62) ? (byte)'+' : (byte)'/';
    			}
    		}
    		oIndex+=4;
    	}
    	
    	for (int i=oBuf.length-numPad; i<oBuf.length; i+=1) {
    		oBuf[i] = (byte)'=';
    	}
    	return oBuf;
    }

}
