package service;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeoutException;

import utils.RabbitMqConfig;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Classe responsável por receber mensagens do exchange BROKER
 */
public class StockExchangeReceiver extends Thread {
    private static final String EXCHANGE_NAME = "BROKER";

    @Override
    public void run() {
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
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println(" [x] Received '" + message + "' with routing key '" + routingKey + "'");

                //Aqui precisa salvar em um arquivo
                //Caminho para o arquivo 
                String caminhoArquivo = "./bolsa-de-valores/src/main/java/files/livro-de-ofertas.csv";

                // Extrair o tipo da transação e o símbolo da mensagem
                String transactionType = routingKey.contains("compra") ? "compra" : "venda";
                String symbol = routingKey.split("\\.")[1]; 
                message = message.replaceAll("[<>]", "");
                String linha = "<" + transactionType + ";" + symbol + ";" + message + ">";

                try {
                    File file = new File(caminhoArquivo);
                    FileWriter fileWriter = new FileWriter(caminhoArquivo, true);
                    PrintWriter printWriter = new PrintWriter(fileWriter);
                    printWriter.println(linha);
                    printWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
                }

                //Envia ao tópico compra.ativo no exchange "BOLSADEVALORES" uma mensagem noticando que a bolsa de valores
                //recebeu uma ordem de compra
                StockExchangePublisher stockExchangePublisher = new StockExchangePublisher(routingKey, message);
                stockExchangePublisher.start();
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
