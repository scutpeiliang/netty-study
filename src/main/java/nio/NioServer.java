package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务器
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        //配置服务器阻塞模式和端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 6324));
        //serverSocketChannel向selector轮询器注册,selector帮其监听Socket连接
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //selector开始轮询
        while (true) {
            selector.select();       //阻塞直到有事件发生
            //获取所有就绪事件并处理
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    //有新的Socket连上了
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel1.accept();
                    socketChannel.configureBlocking(false);
                    //给新连上的Socket在selector注册读写事件
                    socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    //读事件;进行读操作,读取客户端发来的消息
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int i = socketChannel.read(byteBuffer);
                    if (i != -1) {
                        System.out.println("接收到客户端消息:" + new String(byteBuffer.array(), 0, i));
                        byteBuffer.clear();
                    }
                } else if (selectionKey.isWritable()) {
                    //写事件;进行写操作,给客户端发送消息
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.wrap("我是来自服务器的消息".getBytes());
                    socketChannel.write(byteBuffer);
                }
            }
        }
    }
}
