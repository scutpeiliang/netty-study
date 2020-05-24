package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.MessageProtocol;

/**
 * 编码器，本质上就是一个Handler
 */
public class NettyServerEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        /*//先发长度
        ctx.writeAndFlush(msg.getLen());
        //再发TCP包
        ctx.writeAndFlush(msg.getContent());*/
        //先发长度
        out.writeInt(msg.getLen());
        //再发TCP包
        out.writeBytes(msg.getContent());
    }
}
