package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("localhost", 6324));
        boolean isRun = true;
        while (isRun) {
            selector.select();
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isConnectable()) {
                    SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();
                    while (!socketChannel1.finishConnect()) {
                        System.out.println("连接未完成...");
                    }
                    socketChannel1.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                }
                if (selectionKey.isWritable()) {
                    SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.wrap("来自客户端的消息...".getBytes());
                    socketChannel1.write(byteBuffer);
                }
                if(selectionKey.isReadable()){
                    SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int length = socketChannel1.read(byteBuffer);
                    System.out.println(new String(byteBuffer.array(), 0, length));
                }
            }
        }
    }
}
