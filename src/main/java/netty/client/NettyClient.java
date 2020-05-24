package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty客户端
 */
public class NettyClient {
    public static void main(String[] args) {
        //创建一个线程组
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        //做配置
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientDecoder());
                            ch.pipeline().addLast(new NettyClientEncoder());
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("客户端配置就绪...");
            //开始连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6324).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("客户端出现异常！");
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
