package com.brucepang.prpc.protocol;

/**
 * @author BrucePang
 */
public abstract class PrpcServer {
    protected String serverAddress; // 服务的地址
    protected int serverPort; // 服务的端口

    public PrpcServer(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

  public abstract void  startNettyServer();
}
