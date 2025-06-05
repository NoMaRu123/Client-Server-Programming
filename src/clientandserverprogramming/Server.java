/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clientandserverprogramming;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author <TeephopAlex MacHugh>
 */
public class Server {
    // Responsible for listening to incoming request from client, 
    // and creating a socket object to communicating with them
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    // Responsible for keeping our server running
    public void startServer() {
        try {
            // We want our server to be constantly running until the server socket is closed
            // running indefinitly, so while server isn't closed, wating for client to be connect.
            while (!serverSocket.isClosed()) {
                
                Socket socket = serverSocket.accept();
                        // "accept" is a blocking method, meanning our program will be halted here until a client connect
                        // And when a client does connect a socket object is returned which used to communicate with client.
                System.out.println("A new client has conencted");
                
                // Client Handiler class, Each object of this class will be will be responsible for communicatin g with a client
                // this class will also implement the interface runnable.
                ClientHandler clientHandler = new ClientHandler(socket);
                
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e){
            
        }
    }
    
    // This is so that if the error occurs we jsut want to shut down our server socket
    public void closeSeverSocket(){
        // First we need to make sure that our server socket is not null, cuase if it is then we get a null pointer exception if we called the close method on it.
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
        
    }
}
