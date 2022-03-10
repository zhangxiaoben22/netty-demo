package man.zhangxb.netty.raw.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("Client receiverd:" + msg.toString());

/*
        //循环来回发送，知道超长
        if(msg.toString().length()>100){
            return;
        }
        ctx.writeAndFlush(msg+"_hello");
*/

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("补货到异常:"+cause.getStackTrace());
        super.exceptionCaught(ctx, cause);
    }
}
