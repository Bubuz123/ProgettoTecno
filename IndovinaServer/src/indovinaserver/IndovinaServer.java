/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package indovinaserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

/**
 *
 * @author apuzzo_cristian
 */
public class IndovinaServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException {

        DatagramSocket socket = new DatagramSocket(1234);
        byte receiveByte[] = new byte[1024];
        byte sendByte[] = new byte[1024];

        int numero = Util.Random(1, 20); //Qua devo mettere la parola
        System.out.println("Parola----->" + numero);

        /*DatagramPacket receivePacket1 = new DatagramPacket(receiveByte, receiveByte.length);
        socket.receive(receivePacket1);
        String receiveStr1 = new String(receivePacket1.getData());
        receiveStr1 = receiveStr1.trim(); //pulisce
        System.out.println("Numero tentativi----> " + receiveStr1);
        System.out.println("");
        int tentativi = Integer.parseInt(receiveStr1);*/

        String receiveStr = "";
        Boolean controllo = false;
        
        while (!controllo) { //Continua finchè non hanno indovinato
            DatagramPacket receivePacket = new DatagramPacket(receiveByte, receiveByte.length);
            socket.receive(receivePacket);
            receiveStr = new String(receivePacket.getData());
            receiveStr = receiveStr.trim(); //pulisce
            System.out.println("Client----> " + receiveStr);
            System.out.println("");
            
            String sendStr = "Errato";
            if(Integer.parseInt(receiveStr) == numero)
            {
                controllo = true;
                sendStr = "Giusto!";
            }

            sendByte = sendStr.getBytes();
            InetAddress ip = receivePacket.getAddress(); //ip client
            int port = receivePacket.getPort(); //porta client
            DatagramPacket sendPacket = new DatagramPacket(sendByte, sendByte.length, ip, port);
            socket.send(sendPacket);

        }
    }

}
