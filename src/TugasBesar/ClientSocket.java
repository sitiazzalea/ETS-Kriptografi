package TugasBesar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

//print message user di console socket server (di server) - KELAR
//ketika ada user mau masuk buat key pair (di client) - KELAR
//define command to list all public key (PK)
//- LIST_PUBLIC_KEY. Ketimbang meneruskan pesan ke yang lain, server hanya akan mengembalikan daftar PK ke orang yang meminta - KELAR
//-- Artinya server harus menyimpan list of PK client (server-->ClientHandler), nambah ketika ada client yang connect - KELAR
//define command to send encypted message ke user tertentu pakai PK si user tujuan
//- ENCRYPT(username, e, n, message)
//-- si server cuma ngirim ke user tertentu
//-- kalo yang ga dienkrip terkirim ke semua client
//-- nanti si penerima akan otomatis decrypt(d,n, encryptedMessage)

public class ClientSocket {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private KeyPair keypair;

    public ClientSocket(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.keypair = new KeyPair();//generate key pair here
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
//          send username for the first time
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
//          Send public key
            bufferedWriter.write(keypair.getPublicKeyString());
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();// di sini panggil fungsi encrypt
                bufferedWriter.write(username + ":" + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();// setelah ini panggil fungsi decrypt
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 8000);
        ClientSocket client = new ClientSocket(socket, username); //generate keypair for client
        client.listenForMessage();
        client.sendMessage();
        
    }
}
