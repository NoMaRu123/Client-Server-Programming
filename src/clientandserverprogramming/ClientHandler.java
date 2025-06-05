/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clientandserverprogramming;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author <TeephopAlex MacHugh>
 */
public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader; // used to read data, like messages that have been sent from the client
    private BufferedWriter bufferedWriter; // used to sent data, like messages that have been sent from other clients
    private String clientUsername; // used to represent each client
    
    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));        
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER:" + clientUsername + " Has entered the chatRoom!");
        }catch (IOException e){
            // This will close our socket and the stream
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    
    @Override // This is the method that will have to overide when we implement the runnable interface.
              // #And everything in this run method is what is run on the separate thread
              // We want to listen to the incomming messages. => Blocking operation
    
    public void run() {
        String messageFromClient;
        
        while (socket.isConnected()) {
            try{
                //Because we listening to messages, we basiclly reading from bufferedReader.
                messageFromClient = bufferedReader.readLine(); //the program will holt here until there is a messages from client...? => Blocking Operation <=
                // =>>>> and we need to run this on a separate thread otherwise the app will get stuck on this line of code, unable to continue.
                broadcastMessage(messageFromClient);
            } catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break; // 
            }
        }
    }
    
    
    public void broadcastMessage(String messageToSend){
        // To get message to the other client we can loop through the ArrayList that we has created.
        for(ClientHandler clientHandler : clientHandlers){
            try {
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything( socket, bufferedReader, bufferedWriter);
            }
        }
    }
    
    public void removeClienthandler() {
        clientHandlers.remove(this);
        broadcastMessage("SEVER: " + clientUsername + " Has Left The ROOM");
    }
    
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClienthandler();
        try {
            if(bufferedReader != null) {
                bufferedWriter.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
