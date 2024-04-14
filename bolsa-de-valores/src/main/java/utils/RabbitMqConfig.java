package utils;

import com.rabbitmq.client.ConnectionFactory;

/**
 * Classe responsável por configurar a conexão com o servidor do RabbitMQ
 */
public class RabbitMqConfig {
    private static final String HOST = "gull.rmq.cloudamqp.com";
    private static final String USERNAME = "gugkzyzc";
    private static final String PASSWORD = "g_2i_cDGP5kzu-fFabbs7QPIpmI4uQXF";
    private static final String VIRTUAL_HOST = "gugkzyzc";
    //private static final int PORT = 5672;

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        //factory.setPort(PORT);
        return factory;
    }
}
