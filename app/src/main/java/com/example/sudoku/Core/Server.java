package com.example.sudoku.Core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private ArrayList<Socket> clientSockets;
    private final int port = 2021;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private int totalPlayers;


    public Server(int players) throws IOException {
        this.totalPlayers = players;
        this.clientSockets = new ArrayList<>();
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        System.out.println("Servidor a correr!");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                System.out.println("Cliente ligado: "+ clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
