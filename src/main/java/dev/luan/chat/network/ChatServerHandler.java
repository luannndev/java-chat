package dev.luan.chat.network;

import dev.luan.chat.message.ChatMessage;
import dev.luan.chat.server.Server;
import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ConstantPool;

public class ChatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ChatMessage chatMessage) {
            //Now we must broadcast this message to all clients on the server.
            ByteBuf buffer = ctx.alloc().buffer();
            ChatMessageEncoder encoder = (ChatMessageEncoder) ctx.pipeline().get("encoder");
            encoder.convert(chatMessage, buffer);
            for (Channel channel : Server.INSTANCE.clients) {
                if (channel.isOpen()) {
                    channel.writeAndFlush(buffer.retain());
                }
            }
        }
    }
}