package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Classe responsável por listar os ativos disponíveis para compra e venda presentes na bolsa de valores
 */
public class LivroOfertasRepository {
    
    
    public static void inserirOferta(String tipoTransacao, String codigoAtivo, String message) {
        String caminhoArquivo = "./bolsa-de-valores/src/main/java/files/livro-de-ofertas.csv";

        message = message.replaceAll("[<>]", "");
        String linha = "<" + tipoTransacao + ";" + codigoAtivo + ";" + message + ">";

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

    public static void removerAtivo(String symbol, String message) {
        String caminhoArquivo = "./bolsa-de-valores/src/main/java/files/livro-de-ofertas.csv";
        String arquivoTemporario = "./bolsa-de-valores/src/main/java/files/livro-de-ofertas-temp.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoTemporario));
             BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.contains(symbol)) {
                    bw.write(linha);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler ou escrever no arquivo: " + e.getMessage());
        }

        // Excluir o arquivo temporário após a operação
        File arquivoTemp = new File(arquivoTemporario);
        if (arquivoTemp.delete()) {
            System.out.println("Arquivo temporário excluído com sucesso.");
        } else {
            System.out.println("Erro ao excluir o arquivo temporário.");
        }
        
    }
}
