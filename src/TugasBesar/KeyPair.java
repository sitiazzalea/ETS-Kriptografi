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
public class KeyPair extends PublicKey {
    
    private long p; //rahasia
    
    private long q; //rahasia
    
  //  private long e;  sudah ada dari kelas PublicKey
    
    private long d; //rahasia
    
    private boolean isPrime(long number) { //meriksa bilangan prima
        boolean result = true;
        long p = (long)Math.sqrt(number);		
        for (int i = 2; i <= p; i++) {
            if (number % i == 0) {
                result = false;
                break;
            }
        }
        return result;
    } 
    
    private List<Long> primeList = new ArrayList<>();//array buat prima
    private List<Long> getPrimes(long lower, long upper, List<Long> list){
        for (long i = lower; i <= upper; i++){
            if (isPrime (i)) {
                list.add(i);
            }
        }
        return list;
    }

    private boolean isRelativePrime(long a, long b) {        
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).equals(BigInteger.ONE);
    }
    
    private List<Long> relativePrimeList = new ArrayList<>();
    private List<Long> getRelativePrimes(long upper, List<Long> list){
        for (long i = 3; i < upper; i++){
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
    
    
    public KeyPair() {        
//    buat prime generator antara 1000 s/d 9999, masukkan di ArrayList, agar bisa langsung memilih secara acak 
        getPrimes(10, 99, primeList);
        
//    generate random antara 0 s/d lst.size-1 untuk mendapatkan index p dan q
        this.p = primeList.get(randomPicker(primeList.size()));
        this.q = primeList.get(randomPicker(primeList.size()));
        
//    hitung n = p * q, n telah didefinisikan di super class PublicKey
        n = p * q;
        
//    hitung phi(n) = (p-1)(q-1)
        long totient = (p-1)*(q-1);
        
//    pilih kunci publik e yang relatif prima terhadap phi(n). (Kudu bikin algo generator coprime)
        getRelativePrimes(totient, relativePrimeList); //relative prime generator
        
//    pilih e dari daftar relatf prima, dipilih 10 angka pertama agar nilai e tidak terlalu besar untuk perpangkatan 
        this.e = relativePrimeList.get(randomPicker((int)10));//        
        
//    bangkitkan kunci privat (d) dengan persamaan e*d = 1 mod(phi(n)), di mana [d = (1 + k(phi(n))) / e] adalah integer        
//    pertama cari k
        for (int k = 1; ; k++) {
            if ((1 + k*totient) % e == 0) {
                this.d = (1 + k*totient) / e;
                break;
            }
        }
//      TODO: kudu dibuang, cuma buat testing
//        System.out.printf("n = %d x %d  = %d ,e = %d, d = %d, totient = %d\n",p,q, n, e, d, totient);
    } 
   
    public long decrypt(long cipherText) {
        return (BigInteger.valueOf(cipherText).pow((int)d).mod(BigInteger.valueOf(n))).longValue();
    }
    
}
