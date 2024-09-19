package com.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 消息应答
 */
public class Task02 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //让消息队列持久化 durable : true
        channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
        //从控制台输入信息
        System.out.println("请输入消息：");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //开启消息持久化 MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);
        }
    }
}
