package netty_rpc.consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * 发送调用请求;接收响应并返回给NettyClient
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable<Object> {
    private ChannelHandlerContext context;
    private Object result;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    /**
     * 向提供者发送调用请求，等待结果并返回
     */
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush("call");
        wait();
        return result;
    }

    /**
     * 收到调用结果后给result赋值,然后唤醒call()方法的线程
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg;
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
