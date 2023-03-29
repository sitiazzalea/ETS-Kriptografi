package TugasBesar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket socketServer) {
        this.serverSocket = socketServer;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {  
                Socket socket = serverSocket.accept(); //waiting clients to connect
                System.out.println("A new client has connected!"); //client masuk
                ClientRequestHandler clientHandler = new ClientRequestHandler(socket); // this class will be responsible for communicating
// 

//              A thread is a sequence of instruction within a program that can be executed independently on other code
//              Thread share a memory space
//              When you launch an executable, it is running in a thread within a process
                Thread thread = new Thread(clientHandler);
                thread.start(); //begin the execution

            }

        } catch (IOException e) {

        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException{
//      ServerSocket dari library java
        ServerSocket socketServer = new ServerSocket(8000); //to match to the client who wants to connect, port must be the same
//      Then pass to our server class
        Server server = new Server(socketServer);
        server.startServer();
        
    }
}
