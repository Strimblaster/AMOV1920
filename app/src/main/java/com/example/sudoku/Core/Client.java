package com.example.sudoku.Core;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {
   private Socket socket;
   private final int serverPort = 2021;
    private final int serverPortWelcom = 2022;
   private Player player;
   private PrintWriter out;
   private ObjectOutputStream objectOutputStream;
   private ObjectInputStream objectInputStream;
   private String serverAddress;
   private Grid grid;

   public Client(String IP) throws IOException {
       this.serverAddress = IP;
       player = new Player("Andre");
       socket = new Socket(serverAddress, serverPort);
       objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
       objectInputStream = new ObjectInputStream(socket.getInputStream());
       grid = null;
   }


    @Override
    public void run() {
        ObjectOutputStream obOut = null;
        try {
            obOut = new ObjectOutputStream((new Socket(serverAddress, serverPortWelcom)).getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out = new PrintWriter(obOut);
        out.print("Ola");
        out.flush();

        while (true){
            try {
                String gridJsonR = (String) objectInputStream.readObject();
                Gson gsonR = new Gson();
                grid = gsonR.fromJson(gridJsonR, Grid.class);


                //faz as alterações

                Gson gsonW = new Gson();
                String gridJsonW = gsonW.toJson(grid);
                objectOutputStream.write(gridJsonW.getBytes());
                objectOutputStream.flush();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Grid getGrid(){
        return grid;
    }
}
