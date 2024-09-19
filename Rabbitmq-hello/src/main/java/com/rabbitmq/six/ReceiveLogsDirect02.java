package com.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtils;

public class ReceiveLogsDirect02 {
    private static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] argv) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("disk", false, false, false, null);
        //绑定交换机与队列
        channel.queueBind("disk", EXCHANGE_NAME, "error");

        //接收消息
        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.println("ReceiveLogsDirect02接收的消息："+ new String(message.getBody(),"UTF-8"));
        };

        channel.basicConsume("disk",true,deliverCallback,consumerTag -> {});
    }

}
