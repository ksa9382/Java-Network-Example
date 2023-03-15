package ChannelPipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CustomInboundHandler extends ChannelInboundHandlerAdapter {

//    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName().toString());
    private String name;

    public CustomInboundHandler(String name) {
        this.name = name;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        logger.debug("Inbound Handler[\" + name + \"] is added.");
    }
}
