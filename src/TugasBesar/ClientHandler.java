/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TugasBesar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private String publicKeyUser;
    private final String COMMAND_LIST_PUBLIC_KEY = "LIST_PUBLIC_KEY"; //buat nge-list public key semua user 

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.publicKeyUser = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
//          TO DO: broadcast username and their public key
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient.equals(clientUsername + ":"+ COMMAND_LIST_PUBLIC_KEY)) {
//                    System.out.println("LIST OF PUBLIC KEY");
                    StringBuffer sb = new StringBuffer();
                    sb.append("LIST OF PUBLIC KEY \n");
                    for (ClientHandler clientHandler : clientHandlers){
                        sb.append(clientHandler.clientUsername);
                        sb.append(": ");
                        sb.append(clientHandler.publicKeyUser);
                        sb.append("\n");
                    }
                    System.out.println("testing");
                    this.bufferedWriter.write(sb.toString());
                    this.bufferedWriter.newLine();
                    this.bufferedWriter.flush();
                    
                    continue; //supaya ga ke broadcast                    
               }

//              Sout the message to server (sebagai pembeda antara yang dienkripsi dan belum dienkripsi)
                System.out.println("CLEAR MESSAGE: " + messageFromClient);
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    
    
    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler : clientHandlers){
            try{
                if (!clientHandler.clientUsername.equals(clientUsername)) { //biar ga ngirim ke diri sendiri
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }
            catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                
            }
        }
    }
    
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER:" +clientUsername +"has left the chat!");
        
    }
    
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
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
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
