package netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import netty.MessageProtocol;

/**
 * 服务器的业务逻辑
 * 自定义一个Handler，需要继承Netty的HandlerAdapter
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static int i = 0;       //统计收到客户端消息数

    /**
     * 处理客户端发来的消息，即读操作
     * @param ctx 上下文对象，可以通过它获取很多信息如管道、通道等（通道和管道是1对1关系，前者侧重表达数据的传输，后者侧重数据的处理）
     * @param msg 客户端发来的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtocol messageProtocol = (MessageProtocol)msg;
        System.out.println("收到来自客户端第" + ++i + "条消息:" + new String(messageProtocol.getContent(), CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完毕后进行的操作，比如给客户端回消息
     * @param ctx 上下文对象
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        byte[] content = "来自服务器的回信...".getBytes(CharsetUtil.UTF_8);
        MessageProtocol messageProtocol = new MessageProtocol(content.length, content);
        ctx.writeAndFlush(messageProtocol);
    }

    /**
     * 发生异常时如何处理，一般需要关闭通道
     * @param ctx 上下文对象
     * @param cause 发生的异常
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
