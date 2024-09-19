package com.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtils;

public class ReceiveLogsTopic01 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = "Q1";
        //声明队列
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定交换机和队列
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");

        //接收消息
        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.print("ReceiveLogsTopic01接收的消息："+ new String(message.getBody(),"UTF-8"));
            System.out.println("---接收队列："+ queueName + "绑定健：" + message.getEnvelope().getRoutingKey());

        };

        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
