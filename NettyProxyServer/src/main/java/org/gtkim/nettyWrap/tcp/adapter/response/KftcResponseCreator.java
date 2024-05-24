package org.gtkim.nettyWrap.tcp.adapter.response;

public class KftcResponseCreator implements ResponseCreator{
    @Override
    public String makeResponse(String request) {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(request.substring(0, 4)) // 길이
                .append(request.substring(4, 13))       // 코드
//                .append(request.substring(13, 24))   // 인터페이스ID
                .append("KFTC_0210700011")             // 인터페이스ID
                .append("0210")                        // 전문번호
                .append(request.substring(32, 38))     // 종별코드
                .append(request.substring(38, 47))     // 필러
                .append(request.substring(47, 59))    // 거래고유번호
                .append(request.substring(59, 68));   // 필러2

        return responseBuilder.toString();
    }
}
