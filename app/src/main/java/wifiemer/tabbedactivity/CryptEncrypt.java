package wifiemer.tabbedactivity;

import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.util.*;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import static android.util.Base64.*;

/**
 * Created by Atul on 3/7/2017.
 */
public class CryptEncrypt {

    public byte[] Encrypt(String unEnc) throws Exception
    {
        KeySpec keySpec=new PBEKeySpec("Ragiguddatemple".toCharArray(),"Salty_Ladki".getBytes(),1000,256);


        SecretKeyFactory secretKeyFactory=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secretKey=secretKeyFactory.generateSecret(keySpec);

        byte[] secretKeyBytes=secretKey.getEncoded();

        SecretKeySpec secretKeySpec=new SecretKeySpec(secretKeyBytes,"AES");

        Cipher cipher=Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);

        byte[] encryptBytes=cipher.doFinal(unEnc.getBytes());

        return encryptBytes;

    }

    public byte[] Decrypt(byte[] encrypted) throws Exception
    {
        KeySpec keySpec=new PBEKeySpec("Ragiguddatemple".toCharArray(),"Salty_Ladki".getBytes(),1000,256);


        SecretKeyFactory secretKeyFactory=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secretKey=secretKeyFactory.generateSecret(keySpec);

        byte[] secretKeyBytes=secretKey.getEncoded();


        SecretKeySpec secretKeySpec=new SecretKeySpec(secretKeyBytes,"AES");

        Cipher cipher=Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);

        byte[] decryptBytes=cipher.doFinal(encrypted);

        return decryptBytes;
    }

    public void performOp()
    {
        try {
            String UnEnc="This is the unencrypted text";

            byte[] encryptbytes = Encrypt(UnEnc);
            String encoded= Base64.encodeToString(encryptbytes,DEFAULT);
            System.out.println("er hello I am here " + encoded + " " + encoded.length());

            byte[] decryptbytes=Decrypt(Base64.decode(encoded,DEFAULT));
            System.out.println("rocking "+new String(decryptbytes));
        }
        catch(Exception e)
        {
          System.out.println("Encrypting exception "+e.getMessage());
        }
    }

}
