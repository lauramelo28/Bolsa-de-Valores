import java.util.Scanner;

import repository.AtivoRepository;
import service.BrokerPublisher;
import service.BrokerReceiver;
import service.StockExchangeReceiver;


public class App {

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
                    sellAsset();
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente");
            }
            pause();
        }while(option != 0);

        scanner.close();
    }

    private static void menu(){
        clearScreen();
        System.out.println("--------------------");
        System.out.println("| 0- Sair          |");        
        System.out.println("| 1- Comprar ativo |");
        System.out.println("| 2- Vender ativo  |");
        System.out.println("---------------------");        
        System.out.print("\nDigite a ação que deseja executar: ");
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pause() {
        System.out.println("Pressione 'enter' para continuar");
        scanner.nextLine();
    }

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

    private static void sellAsset(){
    }
}