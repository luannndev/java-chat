package dev.luan.chat.client;

import dev.luan.chat.message.ChatMessage;
import dev.luan.chat.network.ChatClientHandler;
import dev.luan.chat.network.ChatMessageDecoder;
import dev.luan.chat.network.ChatMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class Client {
    private String username;

    public Client(String username) {
        this.username = username;
    }

    private Channel serverChannel;

    public boolean isConnectionOpen() {
        return serverChannel.isOpen();
    }

    public void connect(String host, int port) throws Exception {
        System.out.println("Connecting to " + host + ":" + port + "...");
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast("decoder", new ChatMessageDecoder())
                                    .addLast("handler", new ChatClientHandler())
                                    .addLast("encoder", new ChatMessageEncoder());
                        }
                    });

            ChannelFuture f = bootstrap.connect(host, port).sync();
            System.out.println("Connected...");
            serverChannel = f.channel();
            Scanner scanner = new Scanner(System.in);
            while (isConnectionOpen()) {
                String line = scanner.nextLine();

                serverChannel.writeAndFlush(new ChatMessage(username, line));
            }

            System.err.println("The server shut down! Closing program...");

            Thread.sleep(10000L);

        }
        finally {
            worker.shutdownGracefully();
        }
    }
}