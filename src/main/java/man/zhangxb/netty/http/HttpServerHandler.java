package man.zhangxb.netty.http;
/**
 * @author xiaoben.zhang
 * @create 2022/3/11 16:12
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import man.zhangxb.netty.raw.listener.OwnListerner;

/**
 * 创建自定义助手类
 */
// SimpleChannelInboundHandler: 对于请求来讲，其实相当于[入站，入境]
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        System.out.println("收到从："+ctx.channel().remoteAddress());

        // 获取channel
        Channel channel = ctx.channel();

        if (msg instanceof FullHttpRequest) {

            FullHttpRequest request = (FullHttpRequest)msg;
            //boolean keepAlive = isKeepAlive(request);   //判断当前的连接时否是keepalive的

            //打印输入
            int len = request.content().readableBytes();
            if(len > 0) {
                byte[] content = new byte[len];
                request.content().readBytes(content);
                String contentStr = new String(content, "UTF-8");
                System.out.println("收到："+contentStr);
            }

            ByteBuf byteBuf = ctx.alloc().buffer();

            //输出
            String htmlMessage = "<html><head><title>服务端信息</title></head><body>aaa<body></html>";

            byteBuf.writeBytes(htmlMessage.getBytes());


            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            OwnListerner ownListerner = new OwnListerner();

            ctx.writeAndFlush(response).addListener(ownListerner);

        }


    }

    /**
     * 上面的方法是必须重写的,因为是父类定义的抽象方法。
     *
     * 下面的方法是一些 助手类的执行顺序
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。注册");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。移除");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。活跃");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。不活跃");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channeld读取完毕。。。");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("用户事件触发。。。");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel可写更改");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("补货到异常");
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("助手类添加");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("助手类移除");
        super.handlerRemoved(ctx);
    }

}

