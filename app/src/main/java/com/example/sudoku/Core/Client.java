package com.example.sudoku.Core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {
   private Socket socket;
   private final int serverPort = 2021;
   private Player player;
   private ObjectOutputStream objectOutputStream;
   private ObjectInputStream objectInputStream;
   private String serverAddress;

   public Client(String IP) throws IOException, InterruptedException {
       this.serverAddress = IP;

       player = new Player("André");
       Thread thread = new Thread() {
           public void run() {
               try {
                   socket = new Socket(serverAddress, serverPort);
                   objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       };
       thread.start();
       thread.join();
   }


    @Override
    public void run() {
        try {
            objectOutputStream.writeObject(player);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
