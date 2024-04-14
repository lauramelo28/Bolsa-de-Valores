package utils;

import service.StockExchangeReceiver;

/**
 * Classe responsável por executar o aplicativo da bolsa
 */
public class StockExchangeApp extends Thread {
    public static void main(String[] args) {
        StockExchangeReceiver stockExchangeReceiver = new StockExchangeReceiver();
        stockExchangeReceiver.start();
    }
}