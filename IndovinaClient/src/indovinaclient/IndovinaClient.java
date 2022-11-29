/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package indovinaclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author apuzzo_cristian
 */
public class IndovinaClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress ip = InetAddress.getLocalHost(); //Ip del server è stesso del client (stesso pc)
        byte sendByte[] = new byte[1024];
        byte receiveByte[] = new byte[1024];

        Scanner sc = new Scanner(System.in);
        System.out.println("Benvenuto ad indovina il numero. Inserisci il numero di tentativi");
        String sendStr1 = sc.next();
        System.out.println("");
        sendByte = sendStr1.getBytes();
        int tentativi = Integer.parseInt(sendStr1);
        DatagramPacket sendPacket1 = new DatagramPacket(sendByte, sendByte.length, ip, 1234);
        socket.send(sendPacket1);

        while (tentativi > 0) {
            String sendStr = "";
            do {
                System.out.println("Inserisci numero (1-20)");
                sendStr = sc.next();
            } while (Integer.parseInt(sendStr) > 20 || Integer.parseInt(sendStr) < 1);
            
            sendByte = sendStr.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendByte, sendByte.length, ip, 1234);
            socket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveByte, receiveByte.length);
            socket.receive(receivePacket);
            String receiveStr = new String(receivePacket.getData());
            receiveStr = receiveStr.trim();
            System.out.println(receiveStr);
            System.out.println("");

            if (receiveStr.equals("Giusto!")) {
                tentativi = 0;
            }

            tentativi--;

        }

        System.out.println("Fine!");
    }

}
