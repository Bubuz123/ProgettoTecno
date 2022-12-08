/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package indovinaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apuzzo_cristian
 */
public class ClientHandler implements Runnable {

    final Socket socket;
    final Scanner scan;
    String name;
    String vincitore = "TEST";
    boolean isLosggedIn;

    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, String name) {
        this.socket = socket;
        scan = new Scanner(System.in);
        this.name = name;
        isLosggedIn = true;

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            log("ClientHander : " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        String received;
        String tmp = "";
        write(output, "Your name : " + name);
        for (ClientHandler c : IndovinaServer.getClients()) {
            tmp += c.name + ",";
        }

        write(output, "Client attivi : " + tmp);
        write(output, "Parola : " + IndovinaServer.parola);
        write(output, "Lunghezza parola : " + IndovinaServer.lunghezza);

        while (!IndovinaServer.isfinito) {
            received = read();

            if (received.length() != IndovinaServer.lunghezza) {
                write(output, "Lunghezza sbagliata");
            } else {
                if (received.contains("Jolly")) {
                    write(output, IndovinaServer.parola);
                    IndovinaServer.isfinito = true;
                } else {
                    String msgast = IndovinaServer.parola;
                    for (int i = 0; i < IndovinaServer.lunghezza; i++) {
                        if (received.charAt(i) != IndovinaServer.parola.charAt(i)) {
                            StringBuilder str = new StringBuilder(msgast);
                            str.setCharAt(i, '*');
                            msgast = str.toString();
                        }
                    }
                    if (msgast == IndovinaServer.parola) {
                        vincitore = name;
                        write(output, "Hai vinto!");
                        IndovinaServer.isfinito = true;
                    } else {
                        write(output, msgast);
                    }
                }
            }
        }
        log("Fine");
        Fine();
    }

    
    private void Fine()
    {
        while(true)
        {
            write(output, "Ha vinto : " + vincitore);
            write(output, "Fine");
        }
        //closeSocket();
        //closeStreams();
        //System.exit(0);
    }
    
    private String read() {
        String line = "";
        try {
            line = input.readUTF();
        } catch (IOException ex) {
            log("read : " + ex.getMessage());
        }
        return line;
    }

    private void write(DataOutputStream output, String message) {
        try {
            output.writeUTF(message);
        } catch (IOException ex) {
            log("write : " + ex.getMessage());
        }
    }

    private void closeStreams() {
        try {
            this.input.close();
            this.output.close();
        } catch (IOException ex) {
            log("closeStreams : " + ex.getMessage());
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            log("closeSocket : " + ex.getMessage());
        }
    }

    private void log(String msg) {
        System.out.println(msg);

    }
}
