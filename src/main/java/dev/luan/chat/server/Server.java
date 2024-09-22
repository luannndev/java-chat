package dev.luan.chat.server;

import dev.luan.chat.network.ChatMessageDecoder;
import dev.luan.chat.network.ChatMessageEncoder;
import dev.luan.chat.network.ChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.List;

public class Server {

    public static Server INSTANCE;
    public List<Channel> clients = new ArrayList<>();

    public void start(int port) throws Exception {
        System.out.println("Starting server with port " + port);
        INSTANCE = this;
        //Handles incoming connections
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //Handles traffic
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        public void initChannel(Channel channel) throws Exception {
                            //Register client
                            clients.add(channel);

                            channel.pipeline().addLast("decoder", new ChatMessageDecoder())
                                    .addLast("handler", new ChatServerHandler())
                                    .addLast("encoder", new ChatMessageEncoder());
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY,
                            true)
                    .option(ChannelOption.SO_BACKLOG,
                            100)
                    .option(ChannelOption.SO_KEEPALIVE,
                            true);
            ChannelFuture f = bootstrap.bind(port).sync();

            System.out.println("Started server...");

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}