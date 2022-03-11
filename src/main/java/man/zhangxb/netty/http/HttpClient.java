package man.zhangxb.netty.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import man.zhangxb.netty.raw.listener.OwnListerner;

/**
 * @author xiaoben.zhang
 * @create 2022/3/11 16:12
 */
public class HttpClient {
    private  String host = "127.0.0.1";
    private  int port = 8088;

    public HttpClient(){

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
                            ChannelPipeline pipeline =  ch.pipeline();
                                    //关键信息  string到二进制的转换，不加程序就永远发布出去，也不报异常。。。


                            //大坑  http 一个channle 不能同时作为客户端又为服务端。  即  HttpRequestEncoder  HttpResponseEncoder是冲突的
                            pipeline.addLast(new HttpRequestEncoder());

                            //pipeline.addLast(new HttpRequestDecoder());   //用于解析http报文的handler

                            pipeline.addLast(new HttpResponseDecoder());
                            //pipeline.addLast(new HttpObjectAggregator(65536));   //用于将解析出来的数据封装成http对象，httprequest什么的

                            //pipeline.addLast(new HttpResponseEncoder());   //用于将response编码成httpresponse报文发送

                            pipeline.addLast(new HttpClientHandler());
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
        Channel channel = new HttpClient().start();

/*        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for(;;){
            String line = in.readLine();
            if(line==null){
                continue;
            }
            channel.writeAndFlush(line+"\r\n");
            System.out.println("client发送："+line);
        }*/
        OwnListerner ownListerner = new OwnListerner();
        for(;;){
            //channel.writeAndFlush("我是谁"+"\r\n").addListener(new OwnListerner());


            //channel.writeAndFlush("weareh").addListener(ownListerner);

            ByteBuf b = Unpooled.buffer(10);

            String htmlMessage = "<html><head><title>client端信息</title></head><body>aaa<body></html>";

            b.writeBytes(htmlMessage.getBytes());



/*            FullHttpRequest response = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, b);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, b.readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            channel.writeAndFlush(response).addListener(new OwnListerner());*/




            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                    "/kkkk",Unpooled.copiedBuffer(htmlMessage, CharsetUtil.UTF_8));

            request.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            request.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, b.readableBytes());

            channel.writeAndFlush(request).addListener(ownListerner);



            //channel.writeAndFlush("我是谁"+"\r\n");

            Thread.sleep(500L);
        }
    }
}
