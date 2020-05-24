package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import netty.MessageProtocol;

/**
 * 客户端的业务逻辑
 * 自定义一个Handler，需要继承Netty的HandlerAdapter
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 连接建立、通道就绪后就会触发
     * @param ctx 上下文对象
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {
            //发5条消息
            byte[] content = "来自客户端的消息...".getBytes(CharsetUtil.UTF_8);
            MessageProtocol messageProtocol = new MessageProtocol(content.length, content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    /**
     * 读取服务器发来的消息
     * @param ctx 上下文对象
     * @param msg 消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtocol messageProtocol = (MessageProtocol)msg;
        System.out.println("服务器消息:" + new String(messageProtocol.getContent(), CharsetUtil.UTF_8));
    }

    /**
     * 处理异常
     * @param ctx 上下文对象
     * @param cause 异常
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
