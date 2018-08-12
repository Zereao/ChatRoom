package com.zereao.chatroom.service;

import com.zereao.chatroom.annotation.Autowired;
import com.zereao.chatroom.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * 服务端处理Service
 *
 * @author Zereao
 * @version 2018/08/10 22:18
 */
@Service
public class ServerService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransferService transferService;
    @Autowired
    private BroadcastService broadcastService;

    public void init(InputStream is) {

    }
}
