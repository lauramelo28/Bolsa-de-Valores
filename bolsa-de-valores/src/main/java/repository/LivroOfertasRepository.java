package repository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Classe responsável por listar os ativos disponíveis para compra e venda presentes na bolsa de valores
 */
public class LivroOfertasRepository {
    public static void inserirOferta(String transactionType, String symbol, String message) {
        String caminhoArquivo = "./bolsa-de-valores/src/main/java/files/livro-de-ofertas.csv";
        String linha = "<" + transactionType + ";" + symbol + ";" + message + ">";

        try {
            FileWriter fileWriter = new FileWriter(caminhoArquivo, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(linha);
            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
}
