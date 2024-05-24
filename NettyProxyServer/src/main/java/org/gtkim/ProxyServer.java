package org.gtkim;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.example.nettyWrap.tcp.adapter.AsyncTcpClient;
import org.gtkim.example.nettyWrap.tcp.adapter.AsyncTcpServer;
import org.gtkim.nettyWrap.tcp.adapter.handler.ClientHandler;
import org.gtkim.nettyWrap.tcp.adapter.handler.ProxyClientHandler;
import org.gtkim.nettyWrap.tcp.adapter.handler.ProxyServerHandler;

@Slf4j
@Getter
public class ProxyServer extends AsyncTcpServer{
//    private static final Logger log = LogManager.getLogger(ProxyServer.class);

    private String proxyIp;
    private int proxyPort;

    private String clientIp;
    private int clientPort;

    private AsyncTcpClient proxyClient;
    private AsyncTcpClient client;

    public ProxyServer(int localPort, ProxyServerHandler handler) {
        super(localPort, handler);
//        handler.bindParent(this);
        handler.setParent(this);
    }

//    public String getProxyIp() {
//        return proxyIp;
//    }
//
//    public int getProxyPort() {
//        return proxyPort;
//    }
//
//    public String getClientIp() {
//        return clientIp;
//    }
//
//    public int getClientPort() {
//        return clientPort;
//    }

    public ProxyServer initServer() throws Exception{
        init();

        return this;
    }

    public ProxyServer readyProxy(String proxyIp, int proxyPort)
    throws Exception{
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;

        return this;
    }

    public ProxyServer readyClient(String clientIp, int clientPort) throws Exception {
        this.clientIp = clientIp;
        this.clientPort = clientPort;

        return this;
    }

    public void startProxyClient() throws Exception{
        proxyClient = new AsyncTcpClient(proxyIp, proxyPort, new ProxyClientHandler(this));
        proxyClient.init();

        log.debug("try proxy connecting to [" + proxyClient.getRemoteIp() + ":" + proxyClient.getRemotePort() + "]");
        proxyClient.connect();
        log.debug("proxy host[" + proxyClient.getRemoteIp() + ":" + proxyClient.getRemotePort() + " is connected.");
    }

    public void startClient() throws Exception {
        client = new AsyncTcpClient(clientIp, clientPort, new ClientHandler(this));
        client.init();

        log.debug("try connecting to [" + client.getRemoteIp() + ":" + client.getRemotePort() + "]");
        client.connect();

        if (client.isConnected())
            log.debug("client host[" + client.getRemoteIp() + ":" + client.getRemotePort() + " is connected.");
        else
            log.error("Failed to connect to remote host: [" + client.getRemoteIp() + ":" + client.getRemotePort() + "]");
    }

//    public AsyncTcpClient getProxyClient() {
//        return proxyClient;
//    }
//
//    public AsyncTcpClient getClient() {
//        return client;
//    }

    public boolean reconnectClient() throws Exception{
        startClient();
        return client.isConnected();
    }

    @Override
    public void run() {
        super.run();
    }
}
