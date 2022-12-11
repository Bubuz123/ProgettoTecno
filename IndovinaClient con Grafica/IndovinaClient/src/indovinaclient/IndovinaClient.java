/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package indovinaclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apuzzo_cristian
 */
public class IndovinaClient {

    Scanner scan;
    Socket socket = null;
    DataInputStream input = null;
    DataOutputStream output = null;
    static boolean isfinito = false;
    InetAddress ip;

    public IndovinaClient() throws UnknownHostException, IOException {
        ip = InetAddress.getByName("localhost");
        ip = InetAddress.getByName("192.168.227.1");
        socket = new Socket(ip, 1234);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        scan = new Scanner(System.in);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {

        IndovinaClient client = new IndovinaClient();

        //riceve i messaggi
        client.readMessageThread();

        //invia i messaggi
        client.writeMessageThread();

    }

    private void readMessageThread() {
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String msg = "";
                    try {
                        msg = input.readUTF();
                    } catch (IOException ex) {
                        Logger.getLogger(IndovinaClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    log(msg); //Metodo per visualizzare
                    if (msg.contains("Fine")) {
                        isfinito = true;
                        System.exit(0);
                    }
                }
            }
        });
        readMessage.start();
    }

    private void writeMessageThread() {
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isfinito) {
                    String msg = scan.nextLine();
                    try {
                        output.writeUTF(msg);
                    } catch (IOException ex) {
                        Logger.getLogger(IndovinaClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        sendMessage.start();
    }

    private void log(String msg) {
        System.out.println(msg);
    }

}
