package com.zereao.chatroom.service;

import com.zereao.chatroom.annotation.Service;
import com.zereao.chatroom.entry.TransObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * 消息传输与处理Service
 *
 * @author : Zereao
 * @version : 2018/8/12/14/49  14:49
 */
@Service
public class TransferService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public TransObj getObj(InputStream is) {
        logger.info("-----> 从InputStream中读取输入流...");
        if (is != null) {
            try {
                ObjectInputStream ois = new ObjectInputStream(is);
                Object obj = ois.readObject();
                if (obj instanceof TransObj) {
                    TransObj resultObj = (TransObj) obj;
                    logger.info("-----> 从InputStream中读取到对象。\n{}", resultObj);
                    return resultObj;
                }
            } catch (IOException e) {
                logger.error("-----> 从InputStream中读取输入流出错！\n{}", e.getMessage());
            } catch (ClassNotFoundException e) {
                logger.error("-----> 输入流中不存在Object!\n{}", e.getMessage());
            }
        }
        return null;
    }


}