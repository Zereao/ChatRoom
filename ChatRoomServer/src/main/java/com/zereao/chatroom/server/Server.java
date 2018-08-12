package com.zereao.chatroom.server;

import com.zereao.chatroom.annotation.AutoBean;
import com.zereao.chatroom.annotation.Autowired;
import com.zereao.chatroom.service.ServerService;
import com.zereao.chatroom.util.PropParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Zereao
 * @version 2018/08/10 0:03
 */
public class Server implements AutoBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServerService serverService;

    private ServerSocket serverSocket = null;

    public Server() {
        try {
            InetAddress address = InetAddress.getByName(PropParseUtil.SERVER_IP);
            serverSocket = new ServerSocket(PropParseUtil.SERVER_PORT, 0, address);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void start() {
        try {
            Socket socket = serverSocket.accept();
            InputStream is = socket.getInputStream();
            serverService.init(is);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
