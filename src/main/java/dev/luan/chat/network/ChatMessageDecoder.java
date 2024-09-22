package dev.luan.chat.network;

import dev.luan.chat.message.ChatMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ChatMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> list) throws Exception {
        int senderLength = buffer.readInt();
        String sender = buffer.readCharSequence(senderLength, ChatMessage.CHARSET).toString();
        int messageLength = buffer.readInt();
        String msg = buffer.readCharSequence(messageLength, ChatMessage.CHARSET).toString();
        list.add(new ChatMessage(sender, msg));
    }
}