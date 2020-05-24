package netty_rpc.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty_rpc.api.SimpleService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务消费者作为Netty客户端启动
 */
public class NettyClient {
    private static NettyClientHandler handler = new NettyClientHandler();
    private static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //最关键的一步，通过动态代理的方式，自定义消费者调用simpleService.doSth时的底层逻辑
    private static SimpleService simpleService = (SimpleService)createProxy(SimpleService.class);

    public static void main(String[] args) {
        //连接服务器
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6324).sync();
            //开一个线程测试RPC
            new Thread(() -> {
                System.out.println(simpleService.doSth());
            }).start();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("客户端出现异常！");
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    /**
     * 为该接口创建代理对象
     * @param object 该接口
     * @return
     */
    private static Object createProxy(Class<?> object) {
        return Proxy.newProxyInstance(NettyClient.class.getClassLoader(),
                new Class<?>[]{object}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //进行RPC，获取调用结果
                        return threadPool.submit(handler).get();
                    }
                });
    }
}
