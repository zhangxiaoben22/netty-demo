package man.zhangxb.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;


/**
 * @author xiaoben.zhang
 * @create 2022/3/11 16:12
 */
public class HttpServer {
    public static void main(String[] args) throws Exception {

        // 定义一对线程组
        // 主线程组, 用于接受客户端的连接，但是不做任何处理，跟老板一样，不做事
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 从线程组, 老板线程组会把任务丢给他，让手下线程组去做任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // netty服务器的创建, 辅助工具类，用于服务器通道的一系列配置
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)           //绑定两个线程组
                    .channel(NioServerSocketChannel.class)   //指定NIO的模式
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            // 通过SocketChannel去获得对应的管道
                            ChannelPipeline pipeline = channel.pipeline();

                            // 通过管道，添加handler
                            // HttpServerCodec是由netty自己提供的助手类，可以理解为拦截器
                            // 当请求到服务端，我们需要做解码，响应到客户端做编码

                            //pipeline.addLast("HttpServerCodec", new HttpServerCodec());

/*                            pipeline.addLast(new ReadTimeoutHandler(5));
                            pipeline.addLast(new WriteTimeoutHandler(30));*/

                            //
                            //pipeline.addLast(new HttpRequestDecoder());   //用于解析http报文的handler

                            //pipeline.addLast(new HttpResponseEncoder());   //用于将response编码成httpresponse报文发送

                            //pipeline.addLast(new HttpRequestEncoder());
                            //pipeline.addLast(new HttpResponseDecoder());


                            //这个包含   HttpRequestDecoder  和  HttpResponseEncoder
                            pipeline.addLast(new HttpServerCodec());

                            pipeline.addLast(new HttpObjectAggregator(65536));   //用于将解析出来的数据封装成http对象，httprequest什么的





                            //ch.pipeline().addLast("chunkedWriter", new ChunkedWriteHandler());



                            // 添加自定义的助手类，返回 "hello netty~"
                            pipeline.addLast("customHandler", new HttpServerHandler());
                        }
                    }); // 子处理器，用于处理workerGroup

            // 启动server，并且设置8088为启动的端口号，同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            // 监听关闭的channel，设置位同步方式
            channelFuture.channel().closeFuture().sync();
        } finally {
            //退出线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
