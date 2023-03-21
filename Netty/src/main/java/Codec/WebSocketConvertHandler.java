package Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * MessageToMessageCodec 기초 사용법
 * INBOUND_IN, OUTBOUND_IN 형식 매개변수를 취함
 * INBOUND_IN: InboundHandler에서 메시지를 받거나, OutboundHandler에 메시지를 전송하기 위한 형식
 * OUTBOUND_IN: OutboundHandler에 메시지를 넘기거나, decode()에서 INBOUND_IN 타입의 메시지를 변환하기 위한 형식
 */
public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.getData().duplicate().retain();

        switch (msg.getType()) {
            case BINARY:
                out.add(new BinaryWebSocketFrame(payload));
                break;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, payload));
                break;
            case PING:
                out.add(new PingWebSocketFrame(payload));
                break;
            case PONG:
                out.add(new PongWebSocketFrame(payload));
                break;
            case TEXT:
                out.add(new TextWebSocketFrame(payload));
                break;
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(payload));
                break;
            default:
                throw new IllegalStateException(
                        "Unsupported websocket msg" + msg);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        WebSocketFrame payload = msg.duplicate().retain();

        if (payload instanceof BinaryWebSocketFrame)
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.BINARY, payload.content()));
        else if (payload instanceof CloseWebSocketFrame)
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE, payload.content()));
        else if (payload instanceof PingWebSocketFrame)
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PING, payload.content()));
        else if (payload instanceof PongWebSocketFrame)
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PONG, payload.content()));
        else if (payload instanceof TextWebSocketFrame)
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.TEXT, payload.content()));
        else if (payload instanceof ContinuationWebSocketFrame)
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CONTINUATION, payload.content()));
        else {
            throw new IllegalStateException("Unsupported websocket msg " + msg.content());
        }
    }

    public static final class MyWebSocketFrame {
        public enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        private final FrameType type;
        private final ByteBuf data;

        public MyWebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }

        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }
    }
}
