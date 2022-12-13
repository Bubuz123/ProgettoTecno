/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package indovinaserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author apuzzo_cristian
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IndovinaServer {

    static List<String> parole = new ArrayList();
    static String parola = "";
    static int lunghezza = 0;
    static List clients;
    ServerSocket serverSocket;
    static int numOfUsers = 0;
    static boolean isinizio = true;
    static boolean isfinito = false;
    static int nclient = 0;
    static String vincitore = "Usato il jolly";
    static List<String> Classifica = new ArrayList();
    Socket socket;

    public IndovinaServer() {
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(Constants.PORT);
        } catch (IOException ex) {
            log("Server : " + ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        if (isinizio) {
            inizio();
        }
        IndovinaServer server = new IndovinaServer();
        server.waitConnection();
    }

    private void waitConnection() throws IOException {
        log("Server Running...");

        while (!isfinito && numOfUsers != 0 || isinizio) {
            isinizio = false;
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                log("waitConnection : " + ex.getMessage());
            }

            log("Client accepted : " + socket.getInetAddress());

            numOfUsers++;
            ClientHandler handler = new ClientHandler(socket, "user" + numOfUsers);

            Thread thread = new Thread(handler);
            addClient(handler);
            thread.start();
        }
    }

    public static List<ClientHandler> getClients() {
        return clients;
    }

    private void addClient(ClientHandler client) {
        clients.add(client);
    }

    private static void log(String message) {
        System.out.println(message);
    }

    private static void inizio() {
        try {
            String filePath = new File("Parole.txt").getAbsolutePath();
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                parole.add(data);
            }
            myReader.close();
            
            String filePath2 = new File("Classifica.txt").getAbsolutePath();
            File myObj2 = new File(filePath2);
            Scanner myReader2 = new Scanner(myObj2);
            while (myReader2.hasNextLine()) {
                String data = myReader2.nextLine();
                Classifica.add(data);
            }
            myReader2.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        int x = Util.Random(0, parole.size() - 1);
        parola = parole.get(x);
        lunghezza = parola.length();
        log("Parola: " + parola);
    }

}
