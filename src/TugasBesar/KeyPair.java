/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TugasBesar;

import java.math.*;
import java.security.SecureRandom;
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
    
//fungsi isPrime dan PrimeList dibuat untuk membangkitkan bilangan prima
    public static boolean isPrime(int number) {
        boolean result = true;
        int p = (int)Math.sqrt(number);		
        for (int i = 2; i <= p; i++) {
                if (number % i == 0) {
                        result = false;
                        break;
                }
        }
        return result;
    } 

    private List<Integer> primeList = new ArrayList<>();    
    public static List<Integer> getPrimes(int lower, int upper, List<Integer> list){
        for (int i = lower; i <= upper; i++){
            if (isPrime (i)) {
                list.add(i);
            }
        }
        return list;
    }

    public int randomPicker(int bound){
        SecureRandom rand = new SecureRandom();
        return rand.nextInt(bound);
    }
    

//    public KeyPair(List getPrimes) {
    public KeyPair() {
//    buat prime generator antara 1000 s/d 9999, masukkan di ArrayList, agar bisa langsung memilih secara acak 
        getPrimes(1000, 9999, primeList);
//    generate random antara 0 s/d lst.size-1 untuk mendapatkan index p dan q
        this.p = primeList.get(randomPicker(primeList.size()));
        this.q = primeList.get(randomPicker(primeList.size()));
//    hitung n = p * q
        int n = p * q;
//    hitung phi(n) = (p-1)(q-1)
        int phi_n = (p-1)*(q-1);
//    pilih kunci publik e yang relatif prima terhadap phi(n). (Kudu bikin algo generator coprime)
//    bangkitkan kunci privat dengan persamaan e*d = 1 mod(phi(n)), di mana d = (1 + k(phi(n))) / n adalah integer
    }

//  untuk melihat public key
    public PublicKey getPublicKey() {
        return new PublicKey(e, p * q);
    }

}
