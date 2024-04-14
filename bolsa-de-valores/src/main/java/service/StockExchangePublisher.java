package service;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import utils.RabbitMqConfig;

/**
 * Classe responsável por publicar mensagens em um tópico no exchange "BOLSADEVALORES"
 */
public class StockExchangePublisher extends Thread {
    private String topic;
    private String message;
    private static final String EXCHANGE_NAME = "BOLSADEVALORES";

    public StockExchangePublisher(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    @Override
    public void run(){
        ConnectionFactory factory = RabbitMqConfig.getConnectionFactory();

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            channel.basicPublish(EXCHANGE_NAME, topic, null, message.getBytes("UTF-8"));

            System.out.println(" [x] Sent message for BOLSADEVALORES exchange. Tópico: '" + topic + "' Message: '" + message + "'");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}