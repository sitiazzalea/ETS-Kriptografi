/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TugasBesar;

import java.security.SecureRandom;

/**
 *
 * @author Zaza
 */
public class Main {
//    Aplikasi dijalankan: 
//    generateKeypair();
//    showSelfPublicKey();
//    askForOtherPublicKey();
//    startListeningSocket();
    
//    createCommunicationLoop();
    public static void main(String[] arg) {
        
        System.out.println("TESTING ENKRIPSI DAN DEKRIPSI SATU ANGKA");
        KeyPair keyPair = new KeyPair();
        long plainNum = 65;
        long cipherNum = keyPair.encrypt(plainNum);
        long decryptedNum = keyPair.decrypt(cipherNum);
        System.out.printf("Hasil %s \n",(plainNum == decryptedNum) ? "sama" : "tidak sama");
        System.out.printf("Hasil encrypt %d = %d \n", plainNum, cipherNum);
        System.out.printf("Hasil decrypt %d = %d \n\n\n", cipherNum, decryptedNum); 
        
        System.out.println("TESTING ENKRIPSI DAN DEKRIPSI STRING (MULTIPLE ANGKA), KITA PUNYA PASANGAN KUNCI");
        System.out.println("Public Key: " + keyPair.getPublicKeyString());
        String plainText = "matkul kriptografi";
        System.out.println("Plain text: " + plainText);
        byte[] convInput = Kripto.convStr2Bin(plainText);
        long[] cipher = Kripto.encrypt(keyPair, convInput);
        System.out.print("Cipher text: ");
        for (long l: cipher) {
            System.out.printf("%d - ", l );
        }
        System.out.println();
        byte[] decrypt = Kripto.decrypt(keyPair, cipher);
        System.out.printf("Hasil decrypted text: %s\n\n\n ", Kripto.convBin2Str(decrypt));
        
        
        System.out.println("TESTING ENKRIPSI KETIKA MENERIMA PUBLIC KEY DARI PIHAK LAWAN, KITA TIDAK PUNYA PRIVATE KEY");
//      Misalkan program ini Alice, dan ini public key dari Bob (lawan)
        long eksponenLawan = 23; //d = 47
        long modLawan = 209;
        PublicKey pubKeyLawan = new PublicKey(eksponenLawan, modLawan);
        System.out.println("Public Key lawan: " + pubKeyLawan.getPublicKeyString());
        
//      Misalkan Alice ingin mengirim pesan ke Bob
        String pesan = "its surabaya";
        byte[] convPesan = Kripto.convStr2Bin(pesan);
        long[] cipherPesan = Kripto.encrypt(pubKeyLawan, convPesan);
        System.out.print("Cipher text ke Bob: ");
        for (long l: cipherPesan) {
            System.out.printf("%d - ", l );
        }
        System.out.println();
        
        
    }
}
