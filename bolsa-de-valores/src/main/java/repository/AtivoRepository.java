package repository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class AtivoRepository {
    public static void listarAtivos() {
        System.out.println("Lista de Ativos:");
        System.out.println("--------------------");
        System.out.println("Código | Nome de Pregão | Atividade Principal");
        System.out.println("--------------------");
    
        try {
            Files.lines(Paths.get("./bolsa-de-valores/src/main/java/files/lista-ativos.csv"))
                .skip(1)
                 .forEach(line -> {
                     String[] parts = line.split(";");
                     if (parts.length >= 3) {
                         String codigo = parts[0];
                         String nomePregao = parts[1];
                         String atividadePrincipal = parts[2];
                         System.out.printf("%-6s | %-15s | %s%n", codigo, nomePregao, atividadePrincipal);
                     }
                 });
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo lista-ativos.csv");
            e.printStackTrace();
        }
    }
}
