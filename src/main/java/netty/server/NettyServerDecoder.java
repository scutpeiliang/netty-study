package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import netty.MessageProtocol;

import java.util.List;

/**
 * 解码器，本质上就是一个Handler
 */
public class NettyServerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //先获取TCP包长度
        int len = in.readInt();
        //获取该TCP包
        byte[] content = new byte[len];
        in.readBytes(content);
        //封装成MessageProtocol,加入到out中;Netty会自动将其交给业务Handler进行后续业务处理
        MessageProtocol messageProtocol = new MessageProtocol(len, content);
        out.add(messageProtocol);
    }
}
