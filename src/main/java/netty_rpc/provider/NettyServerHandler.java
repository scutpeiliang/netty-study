package netty_rpc.provider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 接收服务消费者发来的消息，执行对应的方法然后将结果发回给消费者
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ("call".equals(msg)) {
            ctx.writeAndFlush(new SimpleServiceImpl().doSth());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
