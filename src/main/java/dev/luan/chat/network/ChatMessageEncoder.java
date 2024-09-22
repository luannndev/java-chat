package dev.luan.chat.network;

import dev.luan.chat.message.ChatMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ChatMessageEncoder extends MessageToMessageEncoder<ChatMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ChatMessage chatMessage, List<Object> list) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        convert(chatMessage, buffer);
        list.add(buffer.retain());
    }

    public ByteBuf convert(ChatMessage message, ByteBuf buffer) {
        buffer.writeInt(message.sender().length());
        buffer.writeCharSequence(message.sender(), ChatMessage.CHARSET);
        buffer.writeInt(message.message().length());
        buffer.writeCharSequence(message.message(), ChatMessage.CHARSET);
        return buffer;
    }
}