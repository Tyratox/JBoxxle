package ch.tyratox.nksa.jboxxle;

import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class JBoxxleCrypt {
	
	private static final String ALGO = "AES";
	
	public static String encrypt(String Data, String pw) throws Exception {
		byte[] keyValue = pw.getBytes();
        Key key = generateKey(keyValue);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
//        System.out.println(encVal.length + "&" + encVal.length % 16);
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData, String pw) throws Exception {
    	byte[] keyValue = pw.getBytes();
        Key key = generateKey(keyValue);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
//        System.out.println(decordedValue.length + "&" + decordedValue.length % 16);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    private static Key generateKey(byte[] keyValue) throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
}

}
