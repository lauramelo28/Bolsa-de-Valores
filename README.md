# Bolsa-de-Valores
Laboratório de Desenvolvimento de Aplicações Móveis e Distribuidas.
Esse projeto tem como objetivo elaborar um sistema para uma bolsa de valores utilizando RabbitMQ.

## 👩🏻‍💻 Alunas:
* Bárbara Mattioly Andrade  
* Laura Enísia Rodrigues Melo
 
## 👨‍🏫 Professor:
* Rommel Carneiro

## 💻 Para compilação e execução do sistema:
1. Clone o repositório do projeto;
2. Execute o BrokerApp localizado na pasta utils através do "Run" da IDE utilizada, ou via terminal;
3. Execute o StockExchange localizado na pasta utils através do "Run" da IDE utilizada, ou via terminal;
4. Com o Broker e a Bolsa em execução, rodar o App, localizado na raiz da pasta "java"

## 📝 Sobre as classes:
- A classe BrokerPublisher é responsável por publicar mensagens em um tópico no exchange "BROKER";
- A classe BrokerReceiver é responsável por inscrever a fila em um tópico no exchange "BOLSADEVALORES". A fila inscrita no tópico receberá todas as mensagens publicadas no exchange;
- A classe StockExchangePublisher é responsável por publicar mensagens em um tópico no exchange "BOLSADEVALORES";
- A classe StockExchangeReceiver é responsável por inscrever a fila no exchange "BROKER". A fila inscrita no tópico receberá todas as mensagens publicadas no exchange;
