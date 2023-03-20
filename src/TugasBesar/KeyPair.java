/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TugasBesar;

import java.math.*;
import java.util.*;

/**
 *
 * @author Zaza
 */
//Untuk generate Key pair
public class KeyPair {

    private int p; //rahasia

    private int q; //rahasia

    private int e;

    private int d; //rahasia

//    public KeyPair(List primeList) {
    public KeyPair() {
//    buat prime generator antara 1000 s/d 9999, masukkan di ArrayList, aagr bisa langsung memilih secara acak 
//    generate random antara 0 s/d lst.size-1 untuk mendapatkan index p dan q
//    hitung n = p * q
//    hitung phi(n) = (p-1)(q-1)
//    pilih kunci publik e yang relatif prima terhadap phi(n). (Kudu bikin algo generator coprime)
//    bangkitkan kunci privat dengan persamaan e*d = 1 mod(phi(n)), di mana d = (1 + k(phi(n))) / n adalah integer
    }

//  untuk melihat public key
    public PublicKey getPublicKey() {
        return new PublicKey(e, p * q);
    }

}
