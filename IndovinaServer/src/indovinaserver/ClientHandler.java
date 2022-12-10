/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package indovinaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
    boolean isLosggedIn;
    String parolaindovinata = "";
    int tentativi = 0;

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
        IndovinaServer.nclient++;

        write(output, "Client attivi : " + tmp);
        write(output, "Parola : " + IndovinaServer.parola);
        write(output, "Lunghezza parola : " + IndovinaServer.lunghezza);

        for (int i = 0; i < IndovinaServer.lunghezza; i++) {
            parolaindovinata += "*";
        }

        while (!IndovinaServer.isfinito) {
            received = read();

            if (received.length() != IndovinaServer.lunghezza) {
                write(output, "Lunghezza sbagliata");
            } else {
                if (received.contains("Jolly")) {
                    write(output, IndovinaServer.parola);
                    IndovinaServer.isfinito = true;
                } else {
                    tentativi++;
                    String msgast = IndovinaServer.parola;
                    for (int i = 0; i < IndovinaServer.lunghezza; i++) {
                        if (received.charAt(i) != IndovinaServer.parola.charAt(i)) {
                            StringBuilder str = new StringBuilder(msgast);
                            str.setCharAt(i, '*');
                            msgast = str.toString();
                        }
                    }
                    for (int j = 0; j < IndovinaServer.lunghezza; j++) {
                        if (msgast.charAt(j) != '*' && parolaindovinata.charAt(j) == '*') {
                            StringBuilder str2 = new StringBuilder(parolaindovinata);
                            str2.setCharAt(j, msgast.charAt(j));
                            parolaindovinata = str2.toString();
                        }
                    }
                    if (parolaindovinata.equals(IndovinaServer.parola)) {
                        IndovinaServer.vincitore = name;
                        write(output, "Hai vinto!");
                        IndovinaServer.isfinito = true;
                    } else {
                        write(output, parolaindovinata);
                    }
                }
            }
        }
        log("Fine");
        Fine();
    }

    private void Fine() {
        IndovinaServer.nclient--;
        write(output, "Ha vinto : " + IndovinaServer.vincitore);
        write(output, "Fine");
        //PrintClassifica();
        while (true) {
            if (IndovinaServer.nclient == 0) {
                this.isLosggedIn = false;
                closeSocket();
                closeStreams();
                break;
            }
        }
    }
    
    private void PrintClassifica()
    {
        boolean controllo = false;
        for(int i = 0; i<IndovinaServer.Classifica.size()-1; i++)
        {
            String s = IndovinaServer.Classifica.get(i);
            String vett[] = s.split(";");
            if(Integer.parseInt(vett[1]) > tentativi && !controllo)
            {
                controllo = true;
                IndovinaServer.Classifica.add(i, name + ";" + tentativi);
            }
        }
        if(controllo)
        {
            String filePath = new File("Classifica.txt").getAbsolutePath();
            try {
                FileWriter myWriter = new FileWriter(filePath);
                for(int j = 0; j<IndovinaServer.Classifica.size()-1; j++)
                {
                    myWriter.write(IndovinaServer.Classifica.get(j));
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(int i = 0; i<IndovinaServer.Classifica.size()-1; i++)
        {
            String s = IndovinaServer.Classifica.get(i);
            write(output, s);
        }
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
