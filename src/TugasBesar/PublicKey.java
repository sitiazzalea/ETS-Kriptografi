/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TugasBesar;

import java.math.BigInteger;

/**
 *
 * @author Zaza
 */
public class PublicKey {
    protected long e;
    
    protected long n; 
    
    public PublicKey() {}
    
    public PublicKey(long e, long n) {
        this.e = e;
        this.n = n;
    }    
    
    public String getPublicKeyString() {
        StringBuffer sb = new StringBuffer();
        sb.append("e: ");
        sb.append(e);
        sb.append(", n: ");
        sb.append(n);
        return sb.toString();
    }
    
    public long encrypt(long plainText) {
        return (BigInteger.valueOf(plainText).pow((int)e).mod(BigInteger.valueOf(n))).longValue();
    }
    
}
