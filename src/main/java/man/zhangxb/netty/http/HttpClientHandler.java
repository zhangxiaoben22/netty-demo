package man.zhangxb.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * @author xiaoben.zhang
 * @create 2022/3/11 16:12
 */
@ChannelHandler.Sharable
public class HttpClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到从："+ctx.channel().remoteAddress());

        // 获取channel
        Channel channel = ctx.channel();

        if (msg instanceof FullHttpRequest) {

            FullHttpRequest request = (FullHttpRequest)msg;
            //boolean keepAlive = isKeepAlive(request);   //判断当前的连接时否是keepalive的


            int len = request.content().readableBytes();
            if(len > 0) {
                byte[] content = new byte[len];
                request.content().readBytes(content);
                String contentStr = new String(content, "UTF-8");
                System.out.println("收到："+contentStr);
            }

/*
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, b);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, b.readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            ctx.writeAndFlush(response);
*/

        }

        if (msg instanceof FullHttpResponse) {

            FullHttpResponse response = (FullHttpResponse)msg;

            int len = response.content().readableBytes();
            if(len > 0) {
                byte[] content = new byte[len];
                response.content().readBytes(content);
                String contentStr = new String(content, "UTF-8");
                System.out.println("收到："+contentStr);
            }

        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("补货到异常:"+cause.getStackTrace());
        super.exceptionCaught(ctx, cause);

    }
}
