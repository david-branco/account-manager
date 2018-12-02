## Account Manager
Project developed under the discipline of Distributed Reliable Systems of the Master's Degree in Informatics Engineering (University of Minho).<br>
This project consists in the development of a fault tolerant Client/Server pair using a group communication protocol. The application was developed in Java and simulates in a robust way the management of bank accounts by a user.

*Technologies used: Java, Apache Maven, XML, IntelliJ IDEA, GitHub, LaTeX, Texmaker, among others.*

The system has among its main functionalities:
- Replicated interface for fault tolerance using active replication protocol;
- State transfer that allows the resumption of Servers operation without service interruption;
- Client interface (stub) that encapsulates the mechanism of remote invocation and replication;
- Persistent storage of the Server data;
- Incremental state transfer during the recovery of a Server that was temporarily out of service;
- Use of a replication protocol that efficiently uses system resources;
- Minimalist management of bank accounts and movements;
- Among other features.

Representative Models: [Server](https://raw.githubusercontent.com/david-branco/account-manager/master/diagrams/Server.png), [Client](https://raw.githubusercontent.com/david-branco/account-manager/master/diagrams/Client.png), [Communication](https://raw.githubusercontent.com/david-branco/account-manager/master/diagrams/Communication.png), [Account](https://raw.githubusercontent.com/david-branco/account-manager/master/diagrams/Account.png).

---

## Gestão de Contas
Projecto desenvolvido no âmbito da disciplina de Sistemas Distribuídos Confiáveis do Mestrado em Engenharia Informática (Universidade do Minho).<br>
Este projecto consiste no desenvolvimento de um par Cliente/Servidor tolerante a faltas utilizando um protocolo de comunicação em grupo. A aplicação foi desenvolvida em Java e simula de uma forma robusta a gestão de contas bancárias por parte de um utilizador.

*Tecnologias Utilizadas: Java, Apache Maven, XML, IntelliJ IDEA, GitHub, LaTeX, Texmaker entre outras.*

O sistema possui entre as suas funcionalidades principais:
- Interface replicada para tolerância a faltas utilizando o protocolo de replicação activo;
- Transferência de estado que permite a reposição em funcionamento de Servidores sem interrupção do serviço;
- Interface para o Cliente (stub) que encapsula o mecanismo de invocação remota e replicação;
- Armazenamento persistente dos dados dos Servidores;
- Transferência incremental de estado durante a recuperação de um Servidor que esteve temporariamente fora de serviço;
- Utilização de um protocolo de replicação que usa eficientemente os recursos do sistema;
- Gestão minimalista de contas e movimentos bancários;
- Entre outras funcionalidades.

Modelos Representativos: [Servidor](https://raw.githubusercontent.com/david-branco/account-manager/master/diagrams/Server.png), [Cliente](https://raw.githubusercontent.com/david-branco/account-manager/master/diagrams/Client.png), [Comunicação](https://raw.githubusercontent.com/david-branco/account-manager/master/diagrams/Communication.png), [Conta](https://raw.githubusercontent.com/david-branco/account-manager/master/diagrams/Account.png).