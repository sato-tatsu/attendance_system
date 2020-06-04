package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class EncryptUtil {

    public static String getPasswordEncrypt(String plain_pw, String salt)
    {
        String result_pw = "";

        if ( (plain_pw != null) && (!plain_pw.equals("")) )
        {
            byte[] bytes;
            String password = plain_pw + salt;
            try
            {
                bytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
                result_pw = DatatypeConverter.printHexBinary(bytes);
            }
            catch(NoSuchAlgorithmException ex)
            {
            }
        }

        return result_pw;
    }
}
