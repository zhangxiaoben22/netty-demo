package man.zhangxb.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class HelloClient {

    private  String host = "127.0.0.1";
    private  int port = 8088;

    public HelloClient(){

    }

    public Channel start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    //.option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            //关键信息  string到二进制的转换，不加程序就永远发布出去，也不报异常。。。
                            .addLast(new StringEncoder())
                            .addLast(new EchoClientHandler());
                }
            });
            //ChannelFuture f = b.connect().sync();
            ChannelFuture f = b.connect(host,port).sync();

            //f.channel().closeFuture().sync();

            Channel channel =  f.channel();

            return channel;
        } finally {
            //group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        Channel channel = new HelloClient().start();

/*        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for(;;){
            String line = in.readLine();
            if(line==null){
                continue;
            }
            channel.writeAndFlush(line+"\r\n");
            System.out.println("client发送："+line);
        }*/

        for(;;){
            channel.writeAndFlush("我是谁"+"\r\n").sync();
            System.out.println("发送一条消息！");
            Thread.sleep(500L);
        }
    }
}
