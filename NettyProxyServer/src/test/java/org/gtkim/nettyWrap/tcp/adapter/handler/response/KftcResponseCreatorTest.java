package org.gtkim.nettyWrap.tcp.adapter.handler.response;

import org.gtkim.nettyWrap.tcp.adapter.response.KftcResponseCreator;
import org.gtkim.nettyWrap.tcp.adapter.response.ResponseCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

class KftcResponseCreatorTest {
    private final ResponseCreator responseCreator = new KftcResponseCreator();

    @Test
    @DisplayName("KFTC_0200700011 전문 응답생성 테스트")
    public void testMakeResponse1() {
        // given
        String request = "0064DCRUZORCLKFTC_02007000110200700011         000068599305         ";

        // when
        String response = responseCreator.makeResponse(request);

        // then
        assertThat(response.getBytes(StandardCharsets.UTF_8).length).isEqualTo(68);
        assertThat(response).isEqualTo("0064DCRUZORCLKFTC_02107000110210700011         000068599305         ");
    }
}