package bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO服务器
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6324);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String s = reader.readLine();
                    System.out.println(s);
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
