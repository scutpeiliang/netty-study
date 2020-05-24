package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Netty服务器
 */
public class NettyServer {
    public static void main(String[] args) {
        //NioEventLoop本质上就是一个不断执行轮询的线程，加上Group就是线程组
        //BossGroup线程组，轮询处理连接请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //WorkerGroup线程组，轮询处理IO请求
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //新建业务线程组
        EventExecutorGroup businessGroup = new NioEventLoopGroup(8);
        //bootstrap，用于做配置
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workerGroup)      //配置BossGroup和WorkerGroup
                    .channel(NioServerSocketChannel.class)   //BossGroup轮询的是NioServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG, 128)  //ChannelOption参数是Socket的标准参数而不是Netty的参数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerDecoder());
                            ch.pipeline().addLast(new NettyServerEncoder());
                            ch.pipeline().addLast(businessGroup, new NettyServerHandler());      //加入Handler即业务处理逻辑
                        }
                    });
            System.out.println("服务器配置就绪...");
            //绑定端口并进行同步
            ChannelFuture channelFuture = bootstrap.bind(6324).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务器出现异常!");
        } finally {
            //两个线程组优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
