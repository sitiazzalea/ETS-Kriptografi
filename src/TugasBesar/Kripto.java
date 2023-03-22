/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TugasBesar;

import java.nio.*;
import java.util.Arrays;

/**
 *
 * @author Zaza
 */
public class Kripto {
    
    public static String convBin2Str(byte[] data) {
        return new String(data) ;
    }
    
    public static byte[] convStr2Bin(String s) {
        return s.getBytes();
    }
    
    public static long[] encrypt(PublicKey pubKey, byte[] plainText) {
        long[] result = new long[plainText.length];
        for (int i = 0; i < plainText.length; i ++) {
            result[i] = pubKey.encrypt(plainText[i]);
        }

        return result;
    }
    
    public static byte[] decrypt(KeyPair privKey, long[] cipherText) {
        byte[] result = new byte[cipherText.length];
        for (int i = 0; i < cipherText.length; i ++) {
            result[i] = (byte)(privKey.decrypt(cipherText[i]));
        }

        return result;
    }
}
