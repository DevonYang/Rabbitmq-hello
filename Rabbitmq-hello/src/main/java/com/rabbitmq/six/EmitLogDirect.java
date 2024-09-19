package com.rabbitmq.six;

import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

public class EmitLogDirect {
    //交换机名字
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws  Exception {
        Channel channel = RabbitMqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,

                    "warning",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);

        }
    }
}
