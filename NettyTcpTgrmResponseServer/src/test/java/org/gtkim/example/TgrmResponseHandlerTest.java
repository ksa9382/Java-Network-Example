package org.gtkim.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class TgrmResponseHandlerTest {
    @Test
    public void tgrm을_리턴한다() throws Exception{
        // Given
        String workspace  = System.getProperty("user.dir");
        System.out.println("workspace = " + workspace);

        Properties prop = new Properties();
        BufferedReader br = new BufferedReader(new FileReader(workspace + File.separator +
                "src/test/resources/application.properties"));

        prop.load(br);
        int serverPort = Integer.parseInt(prop.getProperty("serverPort"));
        String reponseMode = prop.getProperty("mode");
        String tgrmContent = prop.getProperty("tgrmContent");
        String tgrmEncoding = prop.getProperty("tgrmEncoding");

        EmbeddedChannel channel = new EmbeddedChannel(new TgrmResponseHandler(reponseMode, tgrmContent, tgrmEncoding));
        ByteBuf requestData = Unpooled.copiedBuffer("myRequest".getBytes());
        ByteBuf input = requestData.duplicate();

        boolean result = channel.writeInbound(input.retain());
        assertThat(result).isFalse();

        result = channel.finish();
        assertThat(result).isTrue();

        ByteBuf outRead = channel.readOutbound();
        String outReadStr = outRead.toString(Charset.forName(tgrmEncoding));

        assertThat(outReadStr).isEqualTo("myResponse");
        outRead.release();
    }
}