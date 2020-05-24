package bio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * BIO客户端
 */
public class BioClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 6324);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("你好，服务器。");
        writer.close();
        socket.close();
    }

}
