package com.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtils;

//这是一个工作线程，相当于之前的消费者
public class Work01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //消息的接收
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("接收到的消息："+new String(message.getBody()));
        };

        //消息接收被取消时，执行下面代码
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };
        System.out.println("C1等待接收消息...");
        channel.basicConsume(QUEUE_NAME, deliverCallback,cancelCallback);
    }
}
