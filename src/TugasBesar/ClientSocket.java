package TugasBesar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

//print message user di console socket server (di server) - KELAR
//ketika ada user mau masuk buat key pair (di client) - KELAR
//define command to list all public key (PK)
//- LIST_PUBLIC_KEY. Ketimbang meneruskan pesan ke yang lain, server hanya akan mengembalikan daftar PK ke orang yang meminta - KELAR
//-- Artinya server harus menyimpan list of PK client (server-->ClientHandler), nambah ketika ada client yang connect - KELAR
//bikin map untuk nyimpan pasangan user dan pubkey di memori client - KELAR
//print isi map untuk nge-list pasangan user dan pubkey - KELAR
//define command to send encypted message ke user tertentu pakai PK si user tujuan -KELAR
//- ENCRYPT(username, e, n, message) -KELAR
//-- si server cuma ngirim ke user tertentu
//-- kalo yang ga dienkrip terkirim ke semua client
//-- nanti si penerima akan otomatis decrypt(d,n, encryptedMessage)

public class ClientSocket {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private KeyPair keypair;
    private Map<String, PublicKey> userPKMap = new HashMap<>();
    private final String COMMAND_LIST_PUBLIC_KEY = "LIST_PUBLIC_KEY"; //buat nge-list public key semua user 
    private final String COMMAND_ENCRYPT = "ENCRYPT";

     public static String convBin2Hex(byte[] data) {
        StringBuilder result = new StringBuilder();
        for (byte b : data) {
            result.append(String.format("%02x", b));
        }
        return result.toString();       
    }    

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
     
    private void populateUserPKMap(String userPubKeys) {
        userPKMap.clear();
        StringTokenizer tokenizer = new StringTokenizer(userPubKeys.substring(COMMAND_LIST_PUBLIC_KEY.length()), "|");
//      untuk mendapatkan pasangan nama dan pubkey(e, n)
        while (tokenizer.hasMoreElements()) {
            String individualPubKey = tokenizer.nextToken();
//          memisahkan nama dan public key(e, n)
            StringTokenizer secondToken = new StringTokenizer(individualPubKey, "=");
            if (secondToken.countTokens() == 2) {
                String username = secondToken.nextToken();
                String e_n = secondToken.nextToken();
//               memisahkan e dan n
                StringTokenizer thirdToken = new StringTokenizer(e_n, ",");
                if (thirdToken.countTokens() == 2) {
                    String str_e = thirdToken.nextToken();
                    int e = Integer.parseInt(str_e);
                    String str_n = thirdToken.nextToken();
                    int n = Integer.parseInt(str_n);
                    userPKMap.put(username, new PublicKey(e, n));
                }
            }
        }
    }

    private String printUserPKMap() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, PublicKey> entry : userPKMap.entrySet()) {
            PublicKey pk =  entry.getValue();
            String username = entry.getKey();
            sb.append(username);
            sb.append(": ");
            sb.append(pk.getPublicKeyString());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    private  String encrypt(String message) {
        byte[] convertedCipherPesan;
        int cmdLen = COMMAND_ENCRYPT.length();
        int secondSpacePos = message.indexOf(" ", cmdLen +1);
        String nama = message.substring(cmdLen + 1, secondSpacePos);
        String plainText = message.substring(secondSpacePos + 1);
        if (userPKMap.containsKey(nama)) {
            PublicKey pk = userPKMap.get(nama);
            byte[] convPesan = Kripto.convStr2Bin(plainText);
            long[] cipherPesan = Kripto.encrypt(pk, convPesan);
            convertedCipherPesan = new byte[cipherPesan.length * 8];
            for (int i = 0; i < cipherPesan.length; i++) {
                byte[] tmp = longToBytes(cipherPesan[i]);
                for (int j = 0; j < tmp.length; j++) {
                    convertedCipherPesan[i+j] = tmp[j];
                }
            }
            return convBin2Hex(convertedCipherPesan);
        }
        else {
            return "";
        }
    }
    
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
                if (messageToSend.contains(COMMAND_ENCRYPT)) {
                    messageToSend = encrypt(messageToSend);
                }
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
                        msgFromGroupChat = bufferedReader.readLine();
                        if (msgFromGroupChat.contains(COMMAND_LIST_PUBLIC_KEY)) {
                            populateUserPKMap(msgFromGroupChat);
                            System.out.println(printUserPKMap());
                        }
                        else
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
