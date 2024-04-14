package utils;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqConfig {
    private static final String HOST = "gull.rmq.cloudamqp.com";
    private static final String USERNAME = "gugkzyzc";
    private static final String PASSWORD = "g_2i_cDGP5kzu-fFabbs7QPIpmI4uQXF";
    private static final String VIRTUAL_HOST = "gugkzyzc";

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        return factory;
    }
}
