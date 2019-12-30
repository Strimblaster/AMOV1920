package com.example.sudoku.Core;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;

    private ArrayList<Socket> clientSockets;
    private final int port = 2021;
    private final int portWelcome = 2022;
    private int totalPlayers;
    private Grid grid;


    public Server(int players, Grid grid) throws IOException {
        this.totalPlayers = players;
        this.clientSockets = new ArrayList<>();
        this.serverSocket = new ServerSocket(port);
        this.grid = grid;
        socket = new Socket(InetAddress.getLocalHost(), port);
    }

    @Override
    public void run() {
        Thread thread = new Thread(){
            public void run(){
                while (true){
                    for(int i=1; i<clientSockets.size(); i++){ //nao preciso de enviar para mim logo começa em 1
                        Gson gsonW = new Gson();
                        String gridJsonW = gsonW.toJson(grid);
                        ObjectOutputStream objectOutputStream = null;
                        try {
                            objectOutputStream = (ObjectOutputStream) clientSockets.get(i).getOutputStream();
                            objectOutputStream.write(gridJsonW.getBytes());
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //como vamos saber que jogador é a jogar? alterar o get(1) para get(indice de jogador a jogar)
                    try {
                        ObjectInputStream objectInputStream = (ObjectInputStream) clientSockets.get(1).getInputStream();
                        String gridJsonR = (String) objectInputStream.readObject();
                        Gson gsonR = new Gson();
                        grid = gsonR.fromJson(gridJsonR, Grid.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        try {
            serverSocket = new ServerSocket(portWelcome);
            boolean jaComecou = false;
            while(true){
                Socket clientSocket = serverSocket.accept();

                if(!jaComecou){
                    thread.start();
                    jaComecou = true;
                    clientSockets.add(socket);
                }

                clientSockets.add(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Grid getGrid(){
        return grid;
    }

    public void setGrid(Grid grid){
        this.grid = grid;
    }
}
