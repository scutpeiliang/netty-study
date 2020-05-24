package netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.MessageProtocol;

/**
 * 编码器，本质上就是一个Handler
 */
public class NettyClientEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        //先发长度
        out.writeInt(msg.getLen());
        //再发TCP包
        out.writeBytes(msg.getContent());
    }
}
