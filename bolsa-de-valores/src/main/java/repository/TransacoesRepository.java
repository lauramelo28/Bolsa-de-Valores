package repository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class TransacoesRepository {
       
    public static void inserirTransacao(String data, String codigoAtivo, String message) {
        String caminhoArquivo = "./bolsa-de-valores/src/main/java/files/transacoes.csv";
        String linha = "<" + data + ";" + codigoAtivo + ";" + message + ">";
      
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
