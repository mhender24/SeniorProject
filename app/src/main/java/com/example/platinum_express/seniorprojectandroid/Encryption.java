package com.example.platinum_express.seniorprojectandroid;

import android.util.Base64;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by BGM Engineering on 11/16/2016.
 */

public class Encryption
{
    public static boolean decrypt(String userPassword, String encryptedDBPassword) throws Exception
    {
        String password  = "TrackBGMKey12345";
        int iterationCount = 1000;
        int keyLength = 256;
        int saltLength = keyLength / 8; // same size as key output
        byte[] salt = new byte[saltLength];
        String mySalt = getSalt(encryptedDBPassword);
        salt = mySalt.getBytes();
        String _vector = "8947az34awl34kjq";

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey myKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];

        iv = _vector.getBytes();
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, myKey, ivParams);
        byte[] ciphertext = cipher.doFinal(userPassword.getBytes("UTF-8"));
        String end = Base64.encodeToString(ciphertext,Base64.NO_WRAP);
        String encryptedUserPassword = mySalt + end;
        return validated(encryptedUserPassword, encryptedDBPassword);
    }

    public static String getSalt(String encryptedPassword){
        return encryptedPassword.substring(0, 32);
    }


    public static boolean validated(String encryptedUserPassword, String encryptedDBPassword){
        return encryptedUserPassword.equals(encryptedDBPassword);
    }
}
