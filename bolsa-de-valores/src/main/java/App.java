import java.util.Scanner;

import repository.AtivoRepository;
import service.BrokerPublisher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class App extends Thread {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        int option = 0;
        
        System.out.print("Broker: ");
        String brokerCode = scanner.nextLine();

        do{
            menu();
            option = scanner.nextInt();
            switch(option){
                case 0:
                    break;
                case 1:
                    buyAsset(brokerCode);
                    break;
                case 2:
                    sellAsset(brokerCode);
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente");
            }
            pause();
        }while(option != 0);

        scanner.close();
    }

    //Menu de opções
    private static void menu(){
        clearScreen();
        System.out.println("------------------------");
        System.out.println("| 0- Sair              |");
        System.out.println("| 1- Comprar ativo     |");
        System.out.println("| 2- Vender ativo      |");
        System.out.println("------------------------");
        System.out.print("\nDigite a ação que deseja executar: ");
    }

    //Método para Limpar a tela
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //Método para pausar a execução do programa
    private static void pause() {
        System.out.println("Pressione 'enter' para continuar");
        scanner.nextLine();
    }

    //Método para comprar um ativo
    private static void buyAsset(String broker) {
        AtivoRepository.listarAtivos();

        System.out.println("--------------------");
        System.out.print("Codigo do ativo: ");
        String codigo = scanner.next();

        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();

        System.out.print("Valor: ");
        double valor = scanner.nextDouble();

        String topic = "compra." + codigo;
        String message = "<" + quantidade + ";" + valor + ";"
                            + broker + ">";

        //Publicar a mensagem em um tópico no exchange "BROKER" utilizando thread
        BrokerPublisher brokerPublisher = new BrokerPublisher(topic, message);
        brokerPublisher.start();

        pause();
    }

     //Método para vender um ativo
    private static void sellAsset(String broker) throws IOException{
        
        //buscar no arquivo livro-deofertas os ativos que comprou, para ele selecionar para vender
        String caminhoArquivo = "./bolsa-de-valores/src/main/java/files/livro-de-ofertas.csv";
       
        System.out.println("Ativos disponíveis para venda:");
        System.out.println("--------------------");
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) { // lista todos os ativos que o broker comprou
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.contains("compra")) {
                    String ativo = linha.split(";")[1];
                    System.out.println(ativo);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        System.out.println("--------------------");
        System.out.print("Digite o ativo que deseja vender: ");
        String ativo = scanner.next();
        
        //parametros para o broker
        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();

        System.out.print("Valor: ");
        double valor = scanner.nextDouble();

        String topic = "venda." + ativo;
        String message = "<" + quantidade + ";" + valor + ";"
                            + broker + ">";

        //Publicar a mensagem em um tópico no exchange "BROKER" utilizando thread
        BrokerPublisher brokerPublisher = new BrokerPublisher(topic, message); //arrumar p pegar topico e msg dnv
        brokerPublisher.start();

        pause();
    }
}