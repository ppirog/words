package pl.umcs.oop.words.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Server extends Thread {
    private final List<ClientThread> clients = new ArrayList<>();
    private final WordBag wordBag;
    private ServerSocket serverSocket;


    public Server(int port, WordBag wordBag) {

        this.wordBag = wordBag;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSending() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                broadcast(wordBag.get());
//                System.out.println(wordBag.get());

            }
        }, 0, 500);
    }

    public void run() {
        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                ClientThread thread = new ClientThread(clientSocket, this);
                clients.add(thread);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeClient(ClientThread client) {
        clients.remove(client);
        System.out.println("removed");
    }

    public void broadcast(String message) {
        for (var client : clients)
            client.send(message);

    }

}