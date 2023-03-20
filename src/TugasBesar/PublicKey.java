/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TugasBesar;

/**
 *
 * @author Zaza
 */
public class PublicKey {
    private int e;
    
    private int n;    
    
    public PublicKey(int e, int n) {
        this.e = e;
        this.n = n;
    }    
    
    public String getPublicKey() {
        StringBuffer sb = new StringBuffer();
        sb.append("e: ");
        sb.append(e);
        sb.append(", n: ");
        sb.append(n);
        return sb.toString();
    }
    
}
