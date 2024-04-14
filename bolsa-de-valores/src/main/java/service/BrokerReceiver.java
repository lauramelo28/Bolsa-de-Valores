package service;

import com.rabbitmq.client.*;

import utils.RabbitMqConfig;

//Estabelece conexão com o servidor do RabbitMQ e inscreve a fila em um tópico no exchange BOLSADEVALORES
//Falta transformar em app
public class BrokerReceiver extends Thread {
    private static final String EXCHANGE_NAME = "BOLSADEVALORES";

    public static void receive(String topic){
        ConnectionFactory factory = RabbitMqConfig.getConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, topic);

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C, queue: " + queueName);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");

                //Aqui precisa salvar em um arquivo
                //Chamar a stock receiver
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}