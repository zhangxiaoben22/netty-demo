package man.zhangxb.netty.raw.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author xiaoben.zhang
 * @create 2022/3/10 17:13
 */
public class OwnListerner  implements ChannelFutureListener {
    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if(!future.isSuccess()){
            throw new RuntimeException(future.cause());
        }
        //System.out.println("完成回调！");
    }
}
