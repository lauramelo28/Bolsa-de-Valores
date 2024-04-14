package Service;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import utils.RabbitMqConfig;


/**
 * Classe responsável por receber mensagens do exchange BROKER
 */
public class StockExchangeReceiver extends Thread {
    private static final String EXCHANGE_NAME = "BROKER";

    public static void main(String[] args) {
        ConnectionFactory factory = RabbitMqConfig.getConnectionFactory();

        Connection connection = null;
        Channel channel = null;

        /*
        * Estabelece conexão com o servidor do RabbitMQ e inscreve a fila em um tópico no exchange BROKER
        * A fila inscrita no tópico receberá todas as mensagens publicadas no exchange
        */
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "#");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C, queue: " + queueName);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });

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
