package service;

import com.rabbitmq.client.*;

import repository.LivroOfertasRepository;
import repository.TransacoesRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;
import java.nio.charset.StandardCharsets;

import utils.RabbitMqConfig;

import java.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileReader;

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
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println(" [x] Received '" + message + "' with routing key '" + routingKey + "'");


                // Extrair o tipo da transação e o símbolo da mensagem
                String tipoTransacao = routingKey.contains("compra") ? "compra" : "venda";
                String codigoAtivo = routingKey.split("\\.")[1]; 
                message = message.replaceAll("[<>]", ""); // Remove os caracteres < e > da mensagem
             
                LivroOfertasRepository.inserirOferta(tipoTransacao, codigoAtivo, message);

                //Envia ao tópico compra.ativo no exchange "BOLSADEVALORES" uma mensagem noticando que a bolsa de valores recebeu uma ordem de compra
                StockExchangePublisher stockExchangePublisher = new StockExchangePublisher(routingKey, message);
                stockExchangePublisher.start();

                if(tipoTransacao.equals("venda")) {                  
                                      
                    String[] itemsMessage = message.replaceAll("[<>]", "").split(";");
                    String broker = itemsMessage[2];
                    int amount = Integer.parseInt(itemsMessage[0]);
                    
                    String caminhoArquivo = "./bolsa-de-valores/src/main/java/files/livro-de-ofertas.csv";
                    double valorOrdemCompra = 0.0;
                    double valorOrdemVenda = 0.0;
                    LocalDateTime dataAtual = LocalDateTime.now();
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
                    String dataFormatada = dataAtual.format(formato);
                    String topicoTransacao = "transacao." + codigoAtivo;

                    //Lê o arquivo para comparar os valores da ordem de compra e ordem de venda
                    try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
                        String linha;
                        while ((linha = br.readLine()) != null) {
                            if (linha.contains("compra") && linha.contains(codigoAtivo)) {
                                valorOrdemCompra = (Double.parseDouble(linha.split(";")[3]));                  
                            }
                            if (linha.contains("venda") && linha.contains(codigoAtivo)) {
                                valorOrdemVenda = (Double.parseDouble(linha.split(";")[3]));                  
                            }

                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo: " + e.getMessage());
                    }

                    //Verifica se é uma venda válida. Se for, remove o ativo do livro de ofertas e armazena na tabela transação
                    if(valorOrdemVenda >= valorOrdemCompra){                       
                        LivroOfertasRepository.removerAtivo(codigoAtivo, message);
                        TransacoesRepository.inserirTransacao(dataFormatada, codigoAtivo, message);

                        String messageTransaction = dataFormatada + ";" + broker + ";" + broker + ";" + amount + ";" + valorOrdemVenda + ">";

                        //Envia ao tópico transacao.ativo no exchange "BOLSADEVALORES" uma mensagem noticando que foi efetuada uma transação
                        StockExchangePublisher stockExchangePublisherTransaction = new StockExchangePublisher(topicoTransacao, messageTransaction);
                        stockExchangePublisherTransaction.start();
                    } else{
                        System.out.println("Ordem de venda inválida");
                    }        
                }         
            };

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
                    System.out.println("Error" + e.getMessage());
                }
            }
            if (connection != null && connection.isOpen()) { 
                try {
                    connection.close();
                } catch (IOException e) {
                    System.out.println("Error" + e.getMessage());
                }
            }
        }
    }
}
