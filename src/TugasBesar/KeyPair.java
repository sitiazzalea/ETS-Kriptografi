/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TugasBesar;

import java.math.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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
    
    private boolean isPrime(int number) {
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
    private List<Integer> getPrimes(int lower, int upper, List<Integer> list){
        for (int i = lower; i <= upper; i++){
            if (isPrime (i)) {
                list.add(i);
            }
        }
        return list;
    }

    private boolean isRelativePrime(int a, int b) {        
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).equals(BigInteger.ONE);
    }
    
    private List<Integer> relativePrimeList = new ArrayList<>();
    private List<Integer> getRelativePrimes(int upper, List<Integer> list){
        for (int i = 3; i < upper; i++){
            if (isRelativePrime (i, upper)) {
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
        getPrimes(100, 999, primeList);
//    generate random antara 0 s/d lst.size-1 untuk mendapatkan index p dan q
        this.p = primeList.get(randomPicker(primeList.size()));
        this.q = primeList.get(randomPicker(primeList.size()));
//    hitung n = p * q
        int n = p * q;
//    hitung phi(n) = (p-1)(q-1)
        int totient = (p-1)*(q-1);
//    pilih kunci publik e yang relatif prima terhadap phi(n). (Kudu bikin algo generator coprime)
        getRelativePrimes(totient, relativePrimeList); //relative prime generator
        this.e = relativePrimeList.get(randomPicker((int)10));//pick e from list of relative primes        
//    bangkitkan kunci privat (d) dengan persamaan e*d = 1 mod(phi(n)), di mana [d = (1 + k(phi(n))) / e] adalah integer        
//    pertama cari k
        for (int k = 1; ; k++) {
            if ((1 + k*totient) % e == 0) {
                this.d = (1 + k*totient) / e;
                break;
            }
        }
        System.out.printf("n = %d x %d  = %d ,e = %d, d = %d, totient = %d\n",p,q, n, e, d, totient);
    } 
    
//  untuk melihat public key
    public PublicKey getPublicKey() {
        return new PublicKey(e, p * q);
    }
    
}
