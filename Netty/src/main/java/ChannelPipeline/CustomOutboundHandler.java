package ChannelPipeline;

import com.sun.org.slf4j.internal.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class CustomOutboundHandler extends ChannelOutboundHandlerAdapter {
    private Logger logger = new Logger("CustomOutboundHandler");
    private String name;

    public CustomOutboundHandler(String name) {
        this.name = name;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Outbound Handler[" + name + "] is added.");
    }
}
