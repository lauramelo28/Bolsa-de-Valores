package service;

import com.rabbitmq.client.*;

import utils.RabbitMqConfig;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Classe responsável por receber inscrever a fila em um tópico no exchange BOLSADEVALORES
 * A fila inscrita no tópico receberá todas as mensagens publicadas no exchange
 */
public class BrokerReceiver extends Thread {
    private static final String EXCHANGE_NAME = "BOLSADEVALORES";

    @Override
    public void run(){
        ConnectionFactory factory = RabbitMqConfig.getConnectionFactory();

        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String queueName = channel.queueDeclare().getQueue();

            channel.queueBind(queueName, EXCHANGE_NAME, "#");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C, queue: " + queueName);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println(" [x] Received '" + message + "' with routing key '" + routingKey + "'");
            };

            //Consumo da fila
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

            System.in.read();
        } catch (IOException | TimeoutException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            //Fecha o canal e a comunicação com o rabbit
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
