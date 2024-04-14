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

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
             BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoTemporario))) {
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

        // Renomear o arquivo temporário para substituir o arquivo original
        File arquivoOriginal = new File(caminhoArquivo);
        File arquivoRenomeado = new File(arquivoTemporario);
        if (arquivoRenomeado.renameTo(arquivoOriginal)) {
            System.out.println("Ativo vendido com sucesso: " + symbol);
        } else {
            System.out.println("Erro ao vender o ativo: " + symbol);
        }
        
    }
}
