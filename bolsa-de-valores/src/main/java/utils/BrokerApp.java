package utils;

import service.BrokerReceiver;

/**
 * Classe responsável por executar o aplicativo da corretora
 */
public class BrokerApp extends Thread {
    public static void main(String[] args) {
        BrokerReceiver brokerReceiver = new BrokerReceiver();
        brokerReceiver.start();
    }
}